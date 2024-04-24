package com.Ecommerceservice.orderservice.repository;

import com.Ecommerceservice.orderservice.model.Order;
import com.Ecommerceservice.orderservice.model.OrderLineItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
//    List<OrderLineItems> getItemsByOrderId(Long orderId);
}
