package service;

import com.example.ecommerce.dto.request.ProductRequest;
import com.example.ecommerce.dto.response.ProductResponse;
import com.example.ecommerce.exception.ProductNotFoundException;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private Product sampleProduct;
    private ProductRequest sampleRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleProduct = Product.builder()
                .name("Sample Product")
                .price(new BigDecimal("100.00"))
                .stock(10)
                .category("Category A")
                .build();

        sampleRequest = new ProductRequest();
        sampleRequest.setName("Sample Product");
        sampleRequest.setPrice(new BigDecimal("100.00"));
        sampleRequest.setStock(10);
        sampleRequest.setCategory("Category A");
    }

    @Test
    void getAllProducts_ReturnsPage() {
        Page<Product> productPage = new PageImpl<>(List.of(sampleProduct));
        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);

        Page<ProductResponse> result = productService.getAllProducts(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Sample Product", result.getContent().get(0).getName());

        verify(productRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void searchProducts_ReturnsPage() {
        Page<Product> productPage = new PageImpl<>(List.of(sampleProduct));
        when(productRepository.searchProducts(anyString(), anyString(), any(Pageable.class)))
                .thenReturn(productPage);

        Page<ProductResponse> result = productService.searchProducts("Sample", "Category A", PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Category A", result.getContent().get(0).getCategory());

        verify(productRepository, times(1)).searchProducts(anyString(), anyString(), any(Pageable.class));
    }

    @Test
    void getProductByIdResponse_Found() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        ProductResponse response = productService.getProductByIdResponse(1L);

        assertNotNull(response);
        assertEquals("Sample Product", response.getName());

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProductByIdResponse_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductByIdResponse(1L));

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void createProduct_Success() {
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductResponse response = productService.createProduct(sampleRequest);

        assertNotNull(response);
        assertEquals("Sample Product", response.getName());

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_Success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductResponse response = productService.updateProduct(1L, sampleRequest);

        assertNotNull(response);
        assertEquals("Sample Product", response.getName());

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(1L, sampleRequest));

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        assertDoesNotThrow(() -> productService.deleteProduct(1L));

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProduct_NotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L));

        verify(productRepository, times(1)).existsById(1L);
        verify(productRepository, never()).deleteById(anyLong());
    }
}
