package com.example.ecommerce.dto.response;

import com.example.ecommerce.model.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {

    @NotNull
    private Long id;

    @NotBlank
    private String customerName;

    @NotBlank
    @Email
    private String customerEmail;

    @NotNull
    private LocalDateTime orderDate;

    @NotNull
    private OrderStatus status;

    @NotNull
    private BigDecimal totalAmount;

    @Valid
    @NotNull
    private List<OrderItemResponse> orderItems;
}
