package com.ldh.shoppingmall.service.cart;

import com.ldh.shoppingmall.dto.cart.CartDto;
import com.ldh.shoppingmall.dto.cart.CartResponseDto;

public interface CartService {

    CartDto getCartByUserId(Long userId);
    CartResponseDto addToCart(Long userId, Long productId, int quantity);
    void removeFromCart(Long cartItemId);
    void clearCart(Long userId);

}
