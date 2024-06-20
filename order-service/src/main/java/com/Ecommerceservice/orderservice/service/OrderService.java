package com.Ecommerceservice.orderservice.service;

import com.Ecommerceservice.orderservice.dto.InventoryResponse;
import com.Ecommerceservice.orderservice.config.feignClient;
import com.Ecommerceservice.orderservice.dto.OrderLineItemsDto;
import com.Ecommerceservice.orderservice.dto.OrderRequest;
import com.Ecommerceservice.orderservice.exception.InsufficientInventoryException;
import com.Ecommerceservice.orderservice.model.Order;
import com.Ecommerceservice.orderservice.model.OrderLineItems;
import com.Ecommerceservice.orderservice.repository.OrderRepository;
import feign.FeignException;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final feignClient inventoryClient;
    public Order placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItemsList =  orderRequest.getOrderLineItemsDtoList().stream().map(
                item -> mapToOrderLineItems(item)
        ).toList();
        order.setOrderLineItemsList(orderLineItemsList);
        List<String> skuList = order.getOrderLineItemsList().stream().map(item-> item.getSkuCode()).toList();
        InventoryResponse[] inventoryResponsesArray = inventoryClient.getInventoryBySku(skuList);
        boolean allProductsInStock = Arrays.stream(inventoryResponsesArray).allMatch(InventoryResponse::getIsInStock);
        if (inventoryResponsesArray.length > 0 && inventoryResponsesArray[0].getErrorMessage() != null) {
            throw new InsufficientInventoryException("No such SKU code in Inventory, Please try again later");
        }
        if (allProductsInStock) {
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Some Product is not in Stock, Please try again later");
        }
        return order;
    }


    public List<OrderLineItemsDto> getItemsByOrderId(Long orderId){
        return orderRepository.findById(orderId)
                .map(order -> order.getOrderLineItemsList().stream()
                        .map(this::mapToDto)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }


    private OrderLineItems mapToOrderLineItems(OrderLineItemsDto orderLineItemsDto){
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
    private OrderLineItemsDto mapToDto(OrderLineItems orderLineItems){
        OrderLineItemsDto dto = new OrderLineItemsDto();
        dto.setId(orderLineItems.getId());
        dto.setPrice(orderLineItems.getPrice());
        dto.setQuantity(orderLineItems.getQuantity());
        dto.setSkuCode(orderLineItems.getSkuCode());
        return dto;
    }

}
