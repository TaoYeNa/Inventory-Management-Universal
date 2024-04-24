package com.Ecommerceservice.orderservice.service;

import com.Ecommerceservice.orderservice.dto.OrderLineItemsDto;
import com.Ecommerceservice.orderservice.dto.OrderRequest;
import com.Ecommerceservice.orderservice.model.Order;
import com.Ecommerceservice.orderservice.model.OrderLineItems;
import com.Ecommerceservice.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class OrderService {
    private final OrderRepository orderRepository;
    public Order placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItemsList =  orderRequest.getOrderLineItemsDtoList().stream().map(
                item -> mapToOrderLineItems(item)
        ).toList();
        order.setOrderLineItemsList(orderLineItemsList);
        orderRepository.save(order);
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
