package service;

import com.example.ecommerce.dto.request.OrderItemRequest;
import com.example.ecommerce.dto.request.OrderRequest;
import com.example.ecommerce.dto.response.OrderResponse;
import com.example.ecommerce.exception.InsufficientStockException;
import com.example.ecommerce.exception.InvalidOrderStatusException;
import com.example.ecommerce.exception.ProductNotFoundException;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.model.OrderStatus;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_success() {
        OrderItemRequest itemReq = OrderItemRequest.builder()
                .productId(1L)
                .quantity(2)
                .build();

        OrderRequest request = OrderRequest.builder()
                .customerName("Ali")
                .customerEmail("ali@example.com")
                .orderItems(Collections.singletonList(itemReq))
                .build();

        Product product = Product.builder()
                .name("Product1")
                .price(BigDecimal.valueOf(100))
                .stock(10)
                .category("Category1")
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.setId(1L);
            for (OrderItem item : o.getOrderItems()) {
                item.setId(1L);
                item.setOrder(o);
            }
            return o;
        });

        OrderResponse response = orderService.createOrder(request);

        assertNotNull(response);
        assertEquals("Ali", response.getCustomerName());
        assertEquals(OrderStatus.PENDING, response.getStatus());
        assertEquals(1, response.getOrderItems().size());
        assertEquals(BigDecimal.valueOf(200), response.getTotalAmount());
        verify(productRepository).save(any(Product.class));
        verify(orderRepository).save(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();
        assertEquals("Ali", savedOrder.getCustomerName());
        assertEquals(8, product.getStock()); // stock reduced by 2
    }

    @Test
    void createOrder_duplicateProduct_throws() {
        OrderItemRequest item1 = OrderItemRequest.builder().productId(1L).quantity(1).build();
        OrderItemRequest item2 = OrderItemRequest.builder().productId(1L).quantity(2).build();

        OrderRequest request = OrderRequest.builder()
                .customerName("Ali")
                .customerEmail("ali@example.com")
                .orderItems(Arrays.asList(item1, item2))
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(request);
        });

        assertTrue(ex.getMessage().contains("Duplicate product"));
    }

    @Test
    void createOrder_productNotFound_throws() {
        OrderItemRequest item = OrderItemRequest.builder().productId(1L).quantity(1).build();

        OrderRequest request = OrderRequest.builder()
                .customerName("Ali")
                .customerEmail("ali@example.com")
                .orderItems(Collections.singletonList(item))
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> orderService.createOrder(request));
    }

    @Test
    void createOrder_insufficientStock_throws() {
        OrderItemRequest item = OrderItemRequest.builder().productId(1L).quantity(5).build();

        Product product = Product.builder()
                .stock(3)
                .price(BigDecimal.TEN)
                .build();

        OrderRequest request = OrderRequest.builder()
                .customerName("Ali")
                .customerEmail("ali@example.com")
                .orderItems(Collections.singletonList(item))
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(InsufficientStockException.class, () -> orderService.createOrder(request));
    }

    @Test
    void updateOrderStatus_success() {
        Order order = Order.builder()
                .status(OrderStatus.PENDING)
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        OrderResponse response = orderService.updateOrderStatus(1L, OrderStatus.CONFIRMED);

        assertEquals(OrderStatus.CONFIRMED, response.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void updateOrderStatus_invalidStatus_throws() {
        Order order = Order.builder()
                .status(OrderStatus.CONFIRMED)
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(InvalidOrderStatusException.class, () -> orderService.updateOrderStatus(1L, OrderStatus.CANCELLED));
    }

    @Test
    void cancelOrder_success() {
        Product product = Product.builder()
                .stock(5)
                .build();

        OrderItem item = OrderItem.builder()
                .product(product)
                .quantity(2)
                .build();

        Order order = Order.builder()
                .status(OrderStatus.PENDING)
                .orderItems(Collections.singletonList(item))
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        assertDoesNotThrow(() -> orderService.cancelOrder(1L));
        assertEquals(7, product.getStock()); // stock increased by 2

        verify(orderRepository).save(order);
        verify(productRepository).save(product);
    }

    @Test
    void cancelOrder_invalidStatus_throws() {
        Order order = Order.builder()
                .status(OrderStatus.DELIVERED)
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(InvalidOrderStatusException.class, () -> orderService.cancelOrder(1L));
    }

    @Test
    void getOrdersByCustomerEmail_success() {
        Order order = Order.builder()
                .customerEmail("ali@example.com")
                .build();

        when(orderRepository.findByCustomerEmail("ali@example.com"))
                .thenReturn(Collections.singletonList(order));

        List<OrderResponse> responses = orderService.getOrdersByCustomerEmail("ali@example.com");

        assertEquals(1, responses.size());
        assertEquals("ali@example.com", responses.get(0).getCustomerEmail());
    }
}
