package com.ProductAPI.Productservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data // 这个注解就包含了常见的 getter setter什么的 并且是
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    @NotBlank(message = "Name must not be blank")
    @NotNull(message = "Name must not be null")
    private String name;

    private String description;

    @Positive(message ="Price must be positive")
    @NotNull(message = "Price must not be null")
    private BigDecimal price;
}
