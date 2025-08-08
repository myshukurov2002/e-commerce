package repository;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderStatus;
import com.example.ecommerce.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void findByCustomerEmail_returnsOrders() {
        // Save sample orders
        Order order1 = Order.builder()
                .customerName("Ali")
                .customerEmail("ali@example.com")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.valueOf(100))
                .build();

        Order order2 = Order.builder()
                .customerName("Ali")
                .customerEmail("ali@example.com")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.CONFIRMED)
                .totalAmount(BigDecimal.valueOf(200))
                .build();

        Order order3 = Order.builder()
                .customerName("Bob")
                .customerEmail("bob@example.com")
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.valueOf(150))
                .build();

        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);

        // Test method
        List<Order> aliOrders = orderRepository.findByCustomerEmail("ali@example.com");

        assertThat(aliOrders).isNotEmpty();
        assertThat(aliOrders).hasSize(2);
        assertThat(aliOrders).allMatch(order -> order.getCustomerEmail().equals("ali@example.com"));

        List<Order> bobOrders = orderRepository.findByCustomerEmail("bob@example.com");
        assertThat(bobOrders).hasSize(1);
        assertThat(bobOrders.get(0).getCustomerName()).isEqualTo("Bob");
    }

    @Test
    void findByCustomerEmail_returnsEmpty_whenNoMatch() {
        List<Order> orders = orderRepository.findByCustomerEmail("nonexistent@example.com");
        assertThat(orders).isEmpty();
    }
}
