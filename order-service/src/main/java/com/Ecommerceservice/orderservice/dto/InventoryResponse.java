package com.Ecommerceservice.orderservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponse {
    private String skuCode;
    private Boolean isInStock;
    private Integer quantity;
    private String errorMessage;
}
