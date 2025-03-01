package com.ldh.shoppingmall.dto.cart;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartItemDto {

    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;

    public CartItemDto(Long productId, String productName, int quantity, BigDecimal price) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }
}
