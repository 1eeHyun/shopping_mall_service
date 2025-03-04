package com.ldh.shoppingmall.service.product;

import com.ldh.shoppingmall.dto.product.ProductDto;
import com.ldh.shoppingmall.entity.product.Product;

import java.util.List;

public interface ProductService {

    ProductDto addProduct(ProductDto productDto);
    void removeProduct(Long productId);
    List<Product> getRecommendedProducts(int limit);
    List<Product> searchProductsByName(String name);
    Product findByProductName(String productName);
    Product findById(Long productId);
}
