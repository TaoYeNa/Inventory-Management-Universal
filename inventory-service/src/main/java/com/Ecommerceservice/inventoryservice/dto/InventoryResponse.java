package com.Ecommerceservice.inventoryservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Data
public class InventoryResponse {
    private String skuCode;
    private Integer quantity;
}
