package com.Ecommerceservice.orderservice.service;

import com.Ecommerceservice.orderservice.dto.AIOrderMessage;
import com.Ecommerceservice.orderservice.model.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.aot.hint.TypeReference;
import org.springframework.messaging.handler.annotation.Header;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
@RequiredArgsConstructor
public class AIModelResultConsumer {
    private ObjectMapper objectMapper;
    private static final String AI_MODEL_QUEUE = "ai_model_queue";
    private static final String AI_RESULT_QUEUE = "ai_result_queue";
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = AI_MODEL_QUEUE, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag){
        try{
            AIOrderMessage aiOrderMessage = objectMapper.readValue(message, objectMapper.getTypeFactory().constructCollectionType(List.class, Order.class));
            // 调用模拟的 "外部 API" 方法，并接收返回的 10 条数据
            List<Order> response = simulateExternalAPICall(aiOrderMessage.getOrders());
            String correlationId = aiOrderMessage.getCorrelationId();
            sendProcessedOrdersToResultQueue(response, correlationId, channel, tag);
            log.info("Received " + response.size() + " orders after simulated API call with ID:" + correlationId);
        }
        catch (Exception e){
            log.error("Error processing message from RabbitMQ: " + e.getMessage());
        }
    }

    private void sendProcessedOrdersToResultQueue(List<Order> processedOrders, String correlationId, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        try {
            // 构造处理结果消息
            AIOrderMessage processedMessage = new AIOrderMessage(correlationId, processedOrders);

            // 将处理结果序列化为 JSON
            String messageJson = objectMapper.writeValueAsString(processedMessage);

            // 发送结果到结果队列
            rabbitTemplate.convertAndSend(AI_RESULT_QUEUE, messageJson);

            // 手动发送消息确认（ACK），表示处理成功
            channel.basicAck(tag, false);

            log.info("Successfully sent processed orders to result queue with Correlation ID: {}", correlationId);
        } catch (JsonProcessingException e) {
            log.error("Error serializing processed order message: " + e.getMessage());

            // 处理失败，发送 NACK 以便消息重新处理
            try {
                channel.basicNack(tag, false, true);
            } catch (IOException ioException) {
                log.error("Error sending NACK: " + ioException.getMessage());
            }
        } catch (Exception e) {
            log.error("Error sending processed order message to RabbitMQ: " + e.getMessage());

            // 处理失败，发送 NACK
            try {
                channel.basicNack(tag, false, true);
            } catch (IOException ioException) {
                log.error("Error sending NACK: " + ioException.getMessage());
            }
        }
    }



    public List<Order> simulateExternalAPICall(List<Order> orders) {
        try {
            // 模拟网络延迟或处理时间，等待 1-3 秒
            long delay = 1000 + (long)(Math.random() * 2000);  // 1 到 3 秒
            System.out.println("Simulating external API call with delay: " + delay + "ms");
            Thread.sleep(delay);

            // 随机打乱订单列表
            Collections.shuffle(orders);

            // 返回前10条订单（如果订单少于10条，返回全部）
            return orders.subList(0, Math.min(10, orders.size()));

        } catch (InterruptedException e) {
            // 如果线程被中断，打印错误信息
            System.err.println("Error during simulated API call: " + e.getMessage());
            Thread.currentThread().interrupt();
            return Collections.emptyList();
        }
    }

}
