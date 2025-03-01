package com.ldh.shoppingmall.service.product;

import com.ldh.shoppingmall.dto.product.ProductDto;

public interface ProductService {

    ProductDto addProduct(ProductDto productDto);
    void removeProduct(Long productId);
}
