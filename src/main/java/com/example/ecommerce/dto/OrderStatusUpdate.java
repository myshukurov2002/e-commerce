package com.example.ecommerce.dto;

import com.example.ecommerce.model.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderStatusUpdate {

    @NotNull(message = "Order status is required")
    private OrderStatus status;
}
