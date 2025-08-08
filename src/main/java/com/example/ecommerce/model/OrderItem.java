package com.example.ecommerce.model;

import com.example.ecommerce.component.BaseMapper;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class OrderItem extends BaseMapper {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",
            nullable = false,
            insertable = false,
            updatable = false)
    private Order order;

    @Column(name = "order_id")
    private Long OrderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",
            nullable = false,
            insertable = false,
            updatable = false)
    private Product product;

    @Column(name = "product_id")
    private Long ProductId;

    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}