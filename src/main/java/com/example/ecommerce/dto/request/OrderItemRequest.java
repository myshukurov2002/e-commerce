package com.example.ecommerce.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemRequest {

    @NotNull
    private Long productId;

    @Min(1)
    private int quantity;
}
