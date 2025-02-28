package com.ldh.shoppingmall.service;

import com.ldh.shoppingmall.dto.ProductDto;
import com.ldh.shoppingmall.entity.Product;
import com.ldh.shoppingmall.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductDto addProduct(ProductDto productDto) {

        if (productRepository.findByProductName(productDto.getProductName()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product with this name already exists.");

        Product product = new Product(productDto.getProductName(),
                productDto.getDescription(),
                productDto.getImageUrl(),
                productDto.getPrice(),
                LocalDateTime.now());

        Product saved = productRepository.save(product);

        return new ProductDto(saved.getProductName(),
                saved.getDescription(),
                saved.getImageUrl(),
                saved.getPrice());
    }


}
