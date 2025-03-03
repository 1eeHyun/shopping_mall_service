package com.ldh.shoppingmall.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CartDto {

    private Long userId;
    private List<CartItemDto> cartItems;
}
