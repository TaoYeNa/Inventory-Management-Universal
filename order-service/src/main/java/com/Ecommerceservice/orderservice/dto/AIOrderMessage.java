package com.Ecommerceservice.orderservice.dto;

import com.Ecommerceservice.orderservice.model.Order;

import java.util.List;

public class AIOrderMessage {
    private String correlationId;
    private List<Order> orders;

    // Constructors, Getters, Setters
    public AIOrderMessage(String correlationId, List<Order> orders) {
        this.correlationId = correlationId;
        this.orders = orders;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
