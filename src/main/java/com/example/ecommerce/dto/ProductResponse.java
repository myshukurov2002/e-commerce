package com.example.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Min(0)
    private BigDecimal price;

    @NotNull
    @Min(0)
    private Integer stock;

    @NotBlank
    private String category;

    @NotNull
    private Boolean isActive;

    @NotNull
    private LocalDateTime createdAt;
}
