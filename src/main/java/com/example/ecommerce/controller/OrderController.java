package com.example.ecommerce.controller;

import com.example.ecommerce.dto.OrderRequest;
import com.example.ecommerce.dto.OrderStatusUpdate;
import com.example.ecommerce.dto.OrderResponse;
import com.example.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@Valid @RequestBody OrderRequest request) {
        return orderService.createOrder(request);
    }

    @PutMapping("/{id}/status")
    public OrderResponse updateOrderStatus(@PathVariable Long id,
                                           @Valid @RequestBody OrderStatusUpdate statusRequest) {
        return orderService.updateOrderStatus(id, statusRequest.getStatus());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
    }

    @GetMapping("/customer/{email}")
    public List<OrderResponse> getOrdersByCustomerEmail(@PathVariable String email) {
        return orderService.getOrdersByCustomerEmail(email);
    }
}
