package com.Ecommerceservice.apiGateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("product-service", r -> r.path("/api/product/**")
                        .uri("lb://product-service"))
                .route("order-service", r -> r.path("/api/orders/**")
                        .uri("lb://order-service"))
                .route("inventory-service" , r -> r.path("/api/inventory/**")
                        .uri("lb://inventory-service"))
//                Discovery Server Route
                .route("discovery-service", r->r.path("/eureka/")
                        .filters(f-> f.setPath("/"))
                        .uri("http://localhost:8761"))
//                Discovery Server Static Resources Route
                .route("discovery-server-static", r-> r.path("/eureka/**")
                        .uri("http://localhost:8761"))
                .build();
    }
}
