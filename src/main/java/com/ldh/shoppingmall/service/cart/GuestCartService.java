package com.ldh.shoppingmall.service.cart;

import com.ldh.shoppingmall.dto.cart.CartResponseDto;

public interface GuestCartService {
    CartResponseDto getCartByUserId();
    CartResponseDto addToCart(Long productId, int quantity);
    void removeFromCart(Long productId);
    void clearCart();
    CartResponseDto updateCartItem(Long productId, int quantity);
}
