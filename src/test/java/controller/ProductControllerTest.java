package controller;

import com.example.ecommerce.controller.ProductController;
import com.example.ecommerce.dto.request.ProductRequest;
import com.example.ecommerce.dto.response.ProductResponse;
import com.example.ecommerce.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductResponse sampleProductResponse;
    private ProductRequest sampleProductRequest;

    @BeforeEach
    void setUp() {
        sampleProductResponse = new ProductResponse();
        sampleProductResponse.setId(1L);
        sampleProductResponse.setName("Sample Product");
        sampleProductResponse.setPrice(new BigDecimal("100.00"));
        sampleProductResponse.setStock(10);
        sampleProductResponse.setCategory("Category A");

        sampleProductRequest = new ProductRequest();
        sampleProductRequest.setName("Sample Product");
        sampleProductRequest.setPrice(new BigDecimal("100.00"));
        sampleProductRequest.setStock(10);
        sampleProductRequest.setCategory("Category A");
    }

    @Test
    void testGetAllProducts() throws Exception {
        Page<ProductResponse> page = new PageImpl<>(List.of(sampleProductResponse));
        when(productService.getAllProducts(ArgumentMatchers.any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/products")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Sample Product"))
                .andExpect(jsonPath("$.content[0].stock").value(10));

        verify(productService, times(1)).getAllProducts(ArgumentMatchers.any(Pageable.class));
    }

    @Test
    void testSearchProducts() throws Exception {
        Page<ProductResponse> page = new PageImpl<>(List.of(sampleProductResponse));
        when(productService.searchProducts(eq("Sample"), eq("Category A"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/products/search")
                        .param("name", "Sample")
                        .param("category", "Category A")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Sample Product"))
                .andExpect(jsonPath("$.content[0].category").value("Category A"));

        verify(productService, times(1)).searchProducts(eq("Sample"), eq("Category A"), any(Pageable.class));
    }

    @Test
    void testGetProductById() throws Exception {
        when(productService.getProductByIdResponse(1L)).thenReturn(sampleProductResponse);

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sample Product"))
                .andExpect(jsonPath("$.price").value(100.00));

        verify(productService, times(1)).getProductByIdResponse(1L);
    }

    @Test
    void testCreateProduct() throws Exception {
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(sampleProductResponse);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleProductRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Sample Product"));

        verify(productService, times(1)).createProduct(any(ProductRequest.class));
    }

    @Test
    void testUpdateProduct() throws Exception {
        when(productService.updateProduct(eq(1L), any(ProductRequest.class))).thenReturn(sampleProductResponse);

        mockMvc.perform(put("/api/v1/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleProductRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sample Product"));

        verify(productService, times(1)).updateProduct(eq(1L), any(ProductRequest.class));
    }

    @Test
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(1L);
    }
}
