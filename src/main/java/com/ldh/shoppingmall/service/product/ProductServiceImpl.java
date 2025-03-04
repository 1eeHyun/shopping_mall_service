package com.ldh.shoppingmall.service.product;

import com.ldh.shoppingmall.dto.product.ProductDto;
import com.ldh.shoppingmall.entity.product.Product;
import com.ldh.shoppingmall.mapper.ProductMapper;
import com.ldh.shoppingmall.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductDto addProduct(ProductDto productDto) {

        if (productRepository.findByProductName(productDto.getProductName()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Product with this name already exists.");

        Product product = productMapper.toEntity(productDto);
        Product saved = productRepository.save(product);
        return productMapper.toDto(saved);
    }

    @Override
    public void removeProduct(Long productId) {

        if (!productRepository.existsById(productId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found.");

        productRepository.deleteById(productId);
    }

    @Override
    public List<Product> getRecommendedProducts(int limit) {
        return productRepository.findTopNProducts(limit);
    }

    @Override
    public Product findByProductName(String productName) {
        return productRepository.findByProductName(productName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find the product."));
    }

    @Override
    public Product findById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find the product."));
    }

    @Override
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByProductNameContainingIgnoreCase(name);
    }
}
