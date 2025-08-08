package com.example.ecommerce.service;

import com.example.ecommerce.exception.ProductNotFoundException;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> searchProducts(String name, String category, Pageable pageable) {
        if (name != null && !name.isEmpty() && category != null && !category.isEmpty()) {
            return productRepository.findByNameContainingAndCategory(name, category, pageable);
        } else if (name != null && !name.isEmpty()) {
            return productRepository.findByNameContaining(name, pageable);
        } else if (category != null && !category.isEmpty()) {
            return productRepository.findByCategory(category, pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }



    public Product createProduct(Product product) {
        product.setCreatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);
        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setStock(productDetails.getStock());
        product.setCategory(productDetails.getCategory());
        product.setActive(productDetails.isActive());
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}