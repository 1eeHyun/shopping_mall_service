package com.ldh.shoppingmall.dto.cart;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartResponseDto {

    private Long cartId;
    private Long userId;
    private List<CartItemDto> cartItems;

    public CartResponseDto(Long cartId, Long userId, List<CartItemDto> cartItems) {
        this.cartId = cartId;
        this.userId = userId;
        this.cartItems = cartItems;
    }
}
