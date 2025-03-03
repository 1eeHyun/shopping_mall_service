package com.ldh.shoppingmall.api;

import com.ldh.shoppingmall.dto.product.ProductDto;
import com.ldh.shoppingmall.mapper.ProductMapper;
import com.ldh.shoppingmall.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductRestController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping("/recommended")
    public List<ProductDto> getRecommendedProducts(@RequestParam(defaultValue = "4") int limit) {

        return productService.getRecommendedProducts(limit)
                .stream()
                .map(productMapper::toDto)  // Product -> ProductDto
                .collect(Collectors.toList());
    }

    @GetMapping("/{productName}")
    public ProductDto getProductByName(@PathVariable String productName) {
        return productMapper.toDto(productService.findByProductName(productName));  // Product -> ProductDto
    }
}
