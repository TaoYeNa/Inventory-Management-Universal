package com.Ecommerceservice.orderservice.controller;

import com.Ecommerceservice.orderservice.dto.ApiResponse;
import com.Ecommerceservice.orderservice.dto.OrderLineItemsDto;
import com.Ecommerceservice.orderservice.dto.OrderRequest;
import com.Ecommerceservice.orderservice.model.Order;
import com.Ecommerceservice.orderservice.model.OrderLineItems;
import com.Ecommerceservice.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
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
    public ResponseEntity<List<OrderLineItemsDto>> getOrderById(@RequestParam("id") Long id) {
        List<OrderLineItemsDto> orderLineItemsList = orderService.getItemsByOrderId(id);
        if (orderLineItemsList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderLineItemsList);
    }


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
