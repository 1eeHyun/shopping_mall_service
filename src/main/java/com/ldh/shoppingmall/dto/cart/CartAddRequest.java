package com.ldh.shoppingmall.dto.cart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartAddRequest {
    private Long productId;
    private int quantity;
}

