package com.Ecommerceservice.orderservice.service;

import com.Ecommerceservice.orderservice.dto.AIOrderMessage;
import com.Ecommerceservice.orderservice.model.Order;
import com.Ecommerceservice.orderservice.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AIModelService {
    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;
    private static final String AI_MODEL_QUEUE = "ai_model_queue";
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final ObjectMapper objectMapper;

    @Transactional
    public void processOrdersWithAI(int pageNumber, int pageSize, String correlationId){
        List<Order> orders = getOrders(pageNumber,pageSize);
        if(!orders.isEmpty()){
            sendOrderListToQueue(orders, correlationId);
        }
        else{
            log.warn("No orders found on page " + pageNumber);
        }
    }

    private void sendOrderListToQueue(List<Order> orders, String correlationId){
        try{
            AIOrderMessage aiOrderMessage = new AIOrderMessage(correlationId, orders);
            String orderListJson = objectMapper.writeValueAsString(aiOrderMessage);
            rabbitTemplate.convertAndSend(AI_MODEL_QUEUE, orderListJson);
            log.info("Successfully sent " + orders.size() + " orders to RabbitMQ queue.");
        } catch(JsonProcessingException e){
            log.error("Error serializing order list: " + e.getMessage());
        }catch(Exception e){
            log.error("Error sending order list to RabbitMQ: " + e.getMessage());
        }
    }

    public List<Order> getOrders(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return orderRepository.findAll(pageable).getContent();  // 分页返回订单数据
    }

    public String getProcessedOrdersFromQueue(String correlationId) {
        try {
            // 从结果队列中获取处理结果
            String result = (String) rabbitTemplate.receiveAndConvert(AI_RESULT_QUEUE);

            if (result != null && result.contains(correlationId)) {
                return result; // 返回处理结果
            } else {
                return "No result available yet for Correlation ID: " + correlationId;
            }
        } catch (Exception e) {
            log.error("Error receiving message from RabbitMQ: {}", e.getMessage());
            return "Error occurred while retrieving result";
        }
    }


    public void sendTestMessage(String message) {
        try {
            // 只发送消息，不期待同步响应
            rabbitTemplate.convertAndSend("foo.bar.test", message);
            log.info("Processed Test Message: " + message);
        } catch (Exception e) {
            log.error("Error processing test message from RabbitMQ: " + e.getMessage());
        }
    }

}
