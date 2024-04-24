package com.Ecommerceservice.inventoryservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;

@Getter
@Setter
@Builder
@Data
public class InventoryRequest {
    @NotBlank(message = "SKU code must not be blank")
    private String skuCode;
    @NotNull(message = "Quantity must be provided")
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    private Integer quantity;
}
