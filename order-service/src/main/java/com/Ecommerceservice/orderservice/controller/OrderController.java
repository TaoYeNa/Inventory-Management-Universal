package com.Ecommerceservice.orderservice.controller;

import com.Ecommerceservice.orderservice.dto.OrderLineItemsDto;
import com.Ecommerceservice.orderservice.dto.OrderRequest;
import com.Ecommerceservice.orderservice.model.Order;
import com.Ecommerceservice.orderservice.model.OrderLineItems;
import com.Ecommerceservice.orderservice.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<Object> placeOrder(@RequestBody OrderRequest orderRequest){
        Order order = orderService.placeOrder(orderRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(order.getId()).toUri();
        return ResponseEntity.created(location).body(order);
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

}
