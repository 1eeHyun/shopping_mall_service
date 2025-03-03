package com.ldh.shoppingmall.mapper;

import com.ldh.shoppingmall.dto.product.ProductDto;
import com.ldh.shoppingmall.entity.product.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductDto productDto) {
        return new Product(
                productDto.getProductName(),
                productDto.getDescription(),
                productDto.getImageUrl(),
                productDto.getPrice()
        );
    }

    public ProductDto toDto(Product product) {
        return new ProductDto(
                product.getProductName(),
                product.getDescription(),
                product.getImageUrl(),
                product.getPrice()
        );
    }
}
