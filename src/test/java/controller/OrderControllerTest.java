package controller;

import com.example.ecommerce.controller.OrderController;
import com.example.ecommerce.dto.request.OrderRequest;
import com.example.ecommerce.dto.request.OrderItemRequest;
import com.example.ecommerce.dto.request.OrderStatusUpdate;
import com.example.ecommerce.dto.response.OrderResponse;
import com.example.ecommerce.model.OrderStatus;
import com.example.ecommerce.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderResponse sampleResponse;
    private OrderRequest sampleRequest;
    private OrderStatusUpdate sampleStatusUpdate;

    @BeforeEach
    void setup() {
        OrderItemRequest itemRequest = OrderItemRequest.builder()
                .productId(1L)
                .quantity(2)
                .build();

        sampleRequest = OrderRequest.builder()
                .customerName("Ali Vali")
                .customerEmail("ali.vali@example.com")
                .orderItems(Collections.singletonList(itemRequest))
                .build();

        sampleResponse = OrderResponse.builder()
                .id(1L)
                .customerName("Ali Vali")
                .customerEmail("ali.vali@example.com")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .totalAmount(new BigDecimal("200.00"))
                .orderItems(Collections.emptyList())
                .build();

        sampleStatusUpdate = OrderStatusUpdate.builder()
                .status(OrderStatus.CONFIRMED)
                .build();
    }

    @Test
    void getAllOrders_shouldReturnList() throws Exception {
        when(orderService.getAllOrders()).thenReturn(Collections.singletonList(sampleResponse));

        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerName").value("Ali Vali"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        verify(orderService, times(1)).getAllOrders();
    }

    @Test
    void getOrderById_shouldReturnOrder() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(sampleResponse);

        mockMvc.perform(get("/api/v1/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Ali Vali"))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(orderService, times(1)).getOrderById(1L);
    }

    @Test
    void createOrder_shouldReturnCreatedOrder() throws Exception {
        when(orderService.createOrder(ArgumentMatchers.any(OrderRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName").value("Ali Vali"))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(orderService, times(1)).createOrder(ArgumentMatchers.any(OrderRequest.class));
    }

    @Test
    void createOrder_shouldReturnBadRequest_whenInvalidRequest() throws Exception {
        // Missing customerName
        String invalidJson = """
                {
                    "customerEmail": "test@example.com",
                    "orderItems": [{"productId": 1, "quantity": 1}]
                }
                """;

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        verify(orderService, times(0)).createOrder(any());
    }

    @Test
    void updateOrderStatus_shouldReturnUpdatedOrder() throws Exception {
        when(orderService.updateOrderStatus(eq(1L), eq(OrderStatus.CONFIRMED))).thenReturn(sampleResponse);

        mockMvc.perform(put("/api/v1/orders/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleStatusUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING")); // Sample response still has PENDING, you can adjust if needed

        verify(orderService, times(1)).updateOrderStatus(1L, OrderStatus.CONFIRMED);
    }

    @Test
    void cancelOrder_shouldReturnNoContent() throws Exception {
        doNothing().when(orderService).cancelOrder(1L);

        mockMvc.perform(delete("/api/v1/orders/1"))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).cancelOrder(1L);
    }

    @Test
    void getOrdersByCustomerEmail_shouldReturnOrders() throws Exception {
        when(orderService.getOrdersByCustomerEmail("ali.vali@example.com"))
                .thenReturn(Collections.singletonList(sampleResponse));

        mockMvc.perform(get("/api/v1/orders/customer/ali.vali@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerEmail").value("ali.vali@example.com"));

        verify(orderService, times(1)).getOrdersByCustomerEmail("ali.vali@example.com");
    }
}
