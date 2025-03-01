package com.ldh.shoppingmall.service.cart;

import com.ldh.shoppingmall.dto.cart.CartDto;
import com.ldh.shoppingmall.dto.cart.CartItemDto;
import com.ldh.shoppingmall.dto.cart.CartResponseDto;
import com.ldh.shoppingmall.entity.cart.Cart;
import com.ldh.shoppingmall.entity.cart.CartItem;
import com.ldh.shoppingmall.entity.product.Product;
import com.ldh.shoppingmall.entity.user.User;
import com.ldh.shoppingmall.repository.cart.CartItemRepository;
import com.ldh.shoppingmall.repository.cart.CartRepository;
import com.ldh.shoppingmall.repository.product.ProductRepository;
import com.ldh.shoppingmall.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    /**
     * Get cart
     */
    @Override
    public CartDto getCartByUserId(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for user: " + userId));

        List<CartItemDto> cartItems = cart.getCartItems().stream()
                .map(item -> new CartItemDto(
                        item.getProduct().getId(),
                        item.getProduct().getProductName(),
                        item.getQuantity(),
                        item.getPrice()
                ))
                .collect(Collectors.toList());

        CartDto cartDto = new CartDto();
        cartDto.setUserId(userId);
        cartDto.setCartItems(cartItems);

        return cartDto;
    }

    /**
     * Add a product to a cart
     */
    @Override
    @Transactional
    public CartResponseDto addToCart(Long userId, Long productId, int quantity) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart(user);
                    return cartRepository.save(newCart);
                });

        CartItem cartItem = new CartItem(cart, product, quantity, product.getPrice());
        cartItemRepository.save(cartItem);

        return convertToDto(cart);
    }

    @Override
    @Transactional
    public void removeFromCart(Long cartItemId) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));

        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for user: " + userId));

        cartItemRepository.deleteAll(cart.getCartItems());
        cart.setCartItems(new ArrayList<>(cart.getCartItems()));
        cart.getCartItems().clear();

        cartRepository.save(cart);
    }

    /**
     * Convert Cart Entity to CartResponseDto
     */
    private CartResponseDto convertToDto(Cart cart) {

        List<CartItemDto> cartItemDtos = cart.getCartItems().stream()
                .map(item -> new CartItemDto(
                        item.getProduct().getId(),
                        item.getProduct().getProductName(),
                        item.getQuantity(),
                        item.getPrice()
                )).collect(Collectors.toList());

        return new CartResponseDto(cart.getId(), cart.getUser().getId(), cartItemDtos);
    }
}
