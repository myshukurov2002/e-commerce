package com.example.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemResponse {

    @NotNull
    private Long id;

    @NotNull
    private ProductResponse product;

    @NotNull
    private int quantity;

    @NotNull
    private BigDecimal unitPrice;

    @NotNull
    private BigDecimal totalPrice;
}
