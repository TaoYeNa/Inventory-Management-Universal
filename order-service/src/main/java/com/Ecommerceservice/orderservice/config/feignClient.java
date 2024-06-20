package com.Ecommerceservice.orderservice.config;

import com.Ecommerceservice.orderservice.dto.InventoryResponse;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "inventory-service")
public interface feignClient {
    @GetMapping("/api/inventory/getInventoryBySku")
    InventoryResponse[] getInventoryBySku(@RequestParam("sku")List<String> sku);
}
