package com.ldh.shoppingmall.service.cart;

import com.ldh.shoppingmall.dto.cart.CartResponseDto;

import java.math.BigDecimal;

public interface CartService {

    CartResponseDto getCartByUserId(Long userId);
    CartResponseDto addToCart(Long userId, Long productId, int quantity);
    CartResponseDto updateCartItem(Long userId, Long productId, int quantity);
    boolean removeFromCart(Long userId, Long cartItemId);
    void clearCart(Long userId);
    boolean isCartEmpty(Long userId);
    BigDecimal getCartTotalAmount(Long userId);
}
