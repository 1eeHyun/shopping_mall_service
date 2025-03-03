package com.ldh.shoppingmall.mapper;

import com.ldh.shoppingmall.dto.cart.CartItemDto;
import com.ldh.shoppingmall.dto.cart.CartResponseDto;
import com.ldh.shoppingmall.entity.cart.Cart;
import com.ldh.shoppingmall.entity.cart.CartItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CartMapper {

    public static CartResponseDto convertToResponseDto(Cart cart) {
        List<CartItemDto> cartItems = cart.getCartItems().stream()
                .map(item -> {
                    if (item.getProduct() == null) {
                        log.error("CartItem product is NULL for item ID: {}", item.getId());
                        return new CartItemDto(
                                null,
                                "Unknown Product",
                                item.getQuantity(),
                                BigDecimal.ZERO
                        );
                    }
                    return new CartItemDto(
                            item.getProduct().getId(),
                            item.getProduct().getProductName(),
                            item.getQuantity(),
                            item.getPrice()
                    );
                })
                .collect(Collectors.toList());

        return new CartResponseDto(cart.getId(), cart.getUser() != null ? cart.getUser().getId() : null, cartItems);
    }


    private static CartItemDto convertCartItemToDto(CartItem item) {
        return new CartItemDto(
                item.getProduct().getId(),
                item.getProduct().getProductName(),
                item.getQuantity(),
                item.getProduct().getPrice()
        );
    }

    public static CartResponseDto convertGuestCartToDto(Map<Long, CartItemDto> guestCart) {
        List<CartItemDto> cartItems = guestCart.values().stream()
                .map(item -> new CartItemDto(
                        item.getProductId(),
                        item.getProductName(),
                        item.getQuantity(),
                        item.getPrice()))
                .collect(Collectors.toList());

        return new CartResponseDto(null, null, cartItems);
    }


}
