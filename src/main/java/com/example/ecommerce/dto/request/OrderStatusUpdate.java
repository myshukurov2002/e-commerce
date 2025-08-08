package com.example.ecommerce.dto.request;

import com.example.ecommerce.model.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderStatusUpdate {

    @NotNull(message = "Order status is required")
    private OrderStatus status;
}
