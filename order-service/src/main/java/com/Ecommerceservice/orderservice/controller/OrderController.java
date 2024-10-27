package com.Ecommerceservice.orderservice.controller;

import com.Ecommerceservice.orderservice.dto.ApiResponse;
import com.Ecommerceservice.orderservice.dto.OrderLineItemsDto;
import com.Ecommerceservice.orderservice.dto.OrderRequest;
import com.Ecommerceservice.orderservice.model.Order;
import com.Ecommerceservice.orderservice.model.OrderLineItems;
import com.Ecommerceservice.orderservice.service.AIModelResultConsumer;
import com.Ecommerceservice.orderservice.service.AIModelService;
import com.Ecommerceservice.orderservice.service.OrderService;
import com.Ecommerceservice.orderservice.service.Receiver;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final AIModelService AIOrderService;
    private final Receiver receiver;
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
//    @CircuitBreaker(name="inventory", fallbackMethod = "fallback")
    public ResponseEntity<ApiResponse> placeOrder(@RequestBody OrderRequest orderRequest) {
        Order order = orderService.placeOrder(orderRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(order.getId()).toUri();
        return ResponseEntity.created(location).body(new ApiResponse<>(true, order, "OrderPlaced Successfully"));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public ResponseEntity<ApiResponse> getOrderById(@RequestParam("id") Long id) {
        List<OrderLineItemsDto> orderLineItemsList = orderService.getItemsByOrderId(id);
        if (orderLineItemsList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ApiResponse<>(true, orderLineItemsList, "Get Order Successfully "));
    }

    // 模拟发送支付消息
    @GetMapping("/testMQ")
    public ResponseEntity<ApiResponse> testMessageQueue() {
        // 在此调用发送支付消息的代码
        AIOrderService.sendTestMessage("This is a Test Rabbit MQ");
        return ResponseEntity.ok(new ApiResponse<>(true, "MQ Worked Properly", "Test MQ Successfully "));
    }

    @GetMapping("/sendAIOrderRequest")
    public ResponseEntity<ApiResponse> sendAIOrderRequest(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>();
        String correlationId = UUID.randomUUID().toString();

        // 异步调用 AI 处理服务
            try {
                // 模拟异步处理
                AIOrderService.processOrdersWithAI(page, limit, correlationId);
            } catch (Exception e) {
                deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request"));
            }


        return ResponseEntity.ok(new ApiResponse<>(true, correlationId,"Ai Request has been sent!"));
    }

//    @GetMapping("/getAIOrderResult")
//    public ResponseEntity<String> getAIOrderResult(@RequestParam("correlationId") String correlationId) {
//        // 从结果队列中尝试获取结果
//        String result = (String) rabbitTemplate.receiveAndConvert(AI_RESULT_QUEUE);
//
//        if (result != null && result.contains(correlationId)) {
//            // 如果找到结果并且结果对应该 Correlation ID
//            return ResponseEntity.ok(result);
//        } else {
//            // 没有找到结果，返回处理中的状态
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Result not available yet for Correlation ID: " + correlationId);
//        }
//    }





    public ResponseEntity<Object> fallback(OrderRequest orderRequest, RuntimeException ex) {
        // Log the exception and return a meaningful error response
        // 日志记录异常
        logger.error("Failed to place order: {}", ex.getMessage());

        // 返回具体错误信息
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", "Service temporarily unavailable");
        errorDetails.put("message", "Failed to place order, please try again later");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorDetails);
    }

}
