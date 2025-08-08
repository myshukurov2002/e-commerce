package repository;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();

        product1 = Product.builder()
                .name("Apple iPhone")
                .price(BigDecimal.valueOf(999.99))
                .stock(50)
                .category("Electronics")
                .build();

        product2 = Product.builder()
                .name("Samsung Galaxy")
                .price(BigDecimal.valueOf(899.99))
                .stock(30)
                .category("Electronics")
                .build();

        productRepository.save(product1);
        productRepository.save(product2);
    }

    @Test
    void searchProducts_ShouldReturnMatchingProducts() {
        Page<Product> page = productRepository.searchProducts("apple", "electronics", PageRequest.of(0, 10));
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getName()).isEqualTo(product1.getName());

        Page<Product> pageNoCategory = productRepository.searchProducts("galaxy", null, PageRequest.of(0, 10));
        assertThat(pageNoCategory.getContent()).hasSize(1);
        assertThat(pageNoCategory.getContent().get(0).getName()).isEqualTo(product2.getName());

        Page<Product> pageNoName = productRepository.searchProducts(null, "electronics", PageRequest.of(0, 10));
        assertThat(pageNoName.getContent()).hasSize(2);
    }
}
