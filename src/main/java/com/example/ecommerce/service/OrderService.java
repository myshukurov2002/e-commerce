package com.example.ecommerce.service;

import com.example.ecommerce.dto.request.OrderItemRequest;
import com.example.ecommerce.dto.request.OrderRequest;
import com.example.ecommerce.dto.response.OrderItemResponse;
import com.example.ecommerce.dto.response.OrderResponse;
import com.example.ecommerce.dto.response.ProductResponse;
import com.example.ecommerce.exception.*;
import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::mapToOrderResponse)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }

    public OrderResponse createOrder(OrderRequest request) {
        validateOrderRequest(request);

        Order order = Order.builder()
                .customerName(request.getCustomerName())
                .customerEmail(request.getCustomerEmail())
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .orderItems(new ArrayList<>())
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;
        Set<Long> productIds = new HashSet<>();

        for (OrderItemRequest itemReq : request.getOrderItems()) {
            if (!productIds.add(itemReq.getProductId())) {
                throw new IllegalArgumentException("Duplicate product in order: " + itemReq.getProductId());
            }

            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + itemReq.getProductId()));

            if (Boolean.FALSE.equals(product.isVisibility()) || product.getStock() <= 0) {
                throw new InsufficientStockException("Product is out of stock: " + product.getName());
            }

            if (product.getStock() < itemReq.getQuantity()) {
                throw new InsufficientStockException("Not enough stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - itemReq.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(product.getPrice())
                    .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity())))
                    .order(order)
                    .build();

            order.getOrderItems().add(orderItem);
            totalAmount = totalAmount.add(orderItem.getTotalPrice());
        }

        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);
        return mapToOrderResponse(savedOrder);
    }

    public OrderResponse updateOrderStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStatusException("Order can only be modified when in PENDING status.");
        }

        order.setStatus(newStatus);
        return mapToOrderResponse(orderRepository.save(order));
    }

    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        if (order.getStatus() == OrderStatus.PENDING || order.getStatus() == OrderStatus.CONFIRMED) {
            order.setStatus(OrderStatus.CANCELLED);
            for (OrderItem item : order.getOrderItems()) {
                Product product = item.getProduct();
                product.setStock(product.getStock() + item.getQuantity());
                productRepository.save(product);
            }
            orderRepository.save(order);
        } else {
            throw new InvalidOrderStatusException("Order cannot be cancelled as it is already " + order.getStatus());
        }
    }

    public List<OrderResponse> getOrdersByCustomerEmail(String email) {
        return orderRepository.findByCustomerEmail(email)
                .stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    private void validateOrderRequest(OrderRequest request) {
        if (request.getCustomerName() == null || request.getCustomerName().isBlank()) {
            throw new IllegalArgumentException("Customer name is required.");
        }
        if (!EMAIL_PATTERN.matcher(request.getCustomerEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        if (request.getOrderItems() == null || request.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item.");
        }
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .customerEmail(order.getCustomerEmail())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .orderItems(order.getOrderItems().stream()
                        .map(item -> OrderItemResponse.builder()
                                .id(item.getId())
                                .product(mapToProductResponse(item.getProduct()))
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .totalPrice(item.getTotalPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(product.getCategory())
                .isActive(product.isVisibility())
                .build();
    }

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
}
