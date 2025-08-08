package com.example.ecommerce;

import com.example.ecommerce.model.CustomerOrder;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    public void setup() {
        Product product = new Product();
        product.setName("Test Product for Order");
        product.setDescription("This is a test product for order tests.");
        product.setPrice(129.99);
        testProduct = productService.createProduct(product);
    }

    @Test
    public void testCreateGetAndUpdateOrder() throws Exception {
        CustomerOrder order = new CustomerOrder();
        order.setOrderDate(LocalDate.now());
        order.setStatus("PENDING");
        order.setProducts(Collections.singletonList(testProduct));

        // Create Order
        String response = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andReturn().getResponse().getContentAsString();

        CustomerOrder createdOrder = objectMapper.readValue(response, CustomerOrder.class);
        Long orderId = createdOrder.getId();

        // Get Order
        mockMvc.perform(get("/api/orders/" + orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.status").value("PENDING"));

        // Update Order Status
        mockMvc.perform(put("/api/orders/" + orderId + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString("SHIPPED")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SHIPPED"));
    }
}