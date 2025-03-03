package com.ldh.shoppingmall.config;

import com.ldh.shoppingmall.entity.product.Product;
import com.ldh.shoppingmall.repository.product.ProductRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Configuration
public class DataInitializerConfig {

    @Bean
    @Transactional
    public ApplicationRunner init(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() == 0) {
                productRepository.save(new Product("Laptop", "High-performance laptop", "images/laptop.jpg", new BigDecimal("1200.00")));
                productRepository.save(new Product("Mouse", "Wireless ergonomic mouse", "images/mouse.jpg", new BigDecimal("25.99")));
                productRepository.save(new Product("Monitor", "27-inch 4K UHD Monitor", "images/monitor.jpg", new BigDecimal("300.00")));
                productRepository.save(new Product("Keyboard", "Mechanical keyboard with RGB", "images/keyboard.jpg", new BigDecimal("75.50")));
            }
        };
    }
}
