package com.ldh.shoppingmall.service;

import com.ldh.shoppingmall.dto.ProductDto;
import com.ldh.shoppingmall.entity.product.Product;
import com.ldh.shoppingmall.repository.ProductRepository;
import com.ldh.shoppingmall.service.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository; // Mock Repository

    @InjectMocks
    private ProductService productService; // Object that is tested

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this); // initialize Mock object
    }

    @Test
    @DisplayName("Add Product Success")
    void Add_Product_ShouldSaveProduct_WhenProductNameIsUnique() {

        // Given
        ProductDto productDto = new ProductDto("newProduct", "description", "imageUrl", new BigDecimal(100.00));
        Product mockSavedProduct = new Product(productDto.getProductName(), productDto.getDescription(), productDto.getImageUrl(), productDto.getPrice());

        // No exists username
        when(productRepository.findByProductName("newProduct")).thenReturn(Optional.empty());

        when(productRepository.save(any(Product.class))).thenReturn(mockSavedProduct);

        // when
        productService.addProduct(productDto);

        // then
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Add Product Fail")
    void register_ShouldNotSaveProduct_WhenUsernameIsNotUnique() {

        // Given
        ProductDto productDto = new ProductDto("existingProduct", "description", "imageUrl", new BigDecimal(100.00));

        when(productRepository.findByProductName("existingProduct")).thenReturn(Optional.of(new Product()));

        // When and Then
        assertThatThrownBy(() -> productService.addProduct(productDto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("409 CONFLICT \"Product with this name already exists.\"");


        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("Remove Product Success")
    void removeUser_ShouldDelete_WhenProductExists() {

        // Given
        Long productId = 1L;
        when(productRepository.existsById(productId)).thenReturn(true);

        // When
        productService.removeProduct(productId);

        // Then
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    @DisplayName("Remove Product Fail")
    void removeUser_ShouldThrowException_WhenProductNotFound() {

        // Given
        Long productId = 1L;
        when(productRepository.existsById(productId)).thenReturn(false);

        // When and Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            productService.removeProduct(productId);
        });

        assertEquals("404 NOT_FOUND \"Product not found.\"", exception.getMessage());
        verify(productRepository, never()).deleteById(any());
    }
}