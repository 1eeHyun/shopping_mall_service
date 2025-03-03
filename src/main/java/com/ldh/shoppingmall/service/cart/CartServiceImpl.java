package com.ldh.shoppingmall.service.cart;

import com.ldh.shoppingmall.dto.cart.CartResponseDto;
import com.ldh.shoppingmall.entity.cart.Cart;
import com.ldh.shoppingmall.entity.cart.CartItem;
import com.ldh.shoppingmall.entity.product.Product;
import com.ldh.shoppingmall.entity.user.User;
import com.ldh.shoppingmall.mapper.CartMapper;
import com.ldh.shoppingmall.repository.cart.CartItemRepository;
import com.ldh.shoppingmall.repository.cart.CartRepository;
import com.ldh.shoppingmall.repository.product.ProductRepository;
import com.ldh.shoppingmall.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.ldh.shoppingmall.mapper.CartMapper.convertToResponseDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    /**
     * Retrieves the cart for the given user. If the user is a guest, returns a session-based cart.
     */
    @Override
    public CartResponseDto getCartByUserId(Long userId) {

        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null for logged-in users.");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createCartForUser(userId));

        return convertToResponseDto(cart);
    }

    /**
     * Adds a product to the cart. For guests, stores items in session.
     */
    @Override
    @Transactional
    public CartResponseDto addToCart(Long userId, Long productId, int quantity) {

        log.info("Request add to Cart - userId: {}, productId: {}, quantity: {}", userId, productId, quantity);

        // Check if the product exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        log.info("Product found: {}", product.getProductName());

        // Check if user cart exists
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createCartForUser(userId));  // generate cart id if there is no cart

        // Add to a cart
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseGet(() -> {
                    log.info("Creating new cart item for user: {}", userId);
                    CartItem newItem = new CartItem(cart, product, 0, product.getPrice());
                    cart.getCartItems().add(newItem);
                    return newItem;
                });

        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItemRepository.save(cartItem);

        log.info("Product {} added successfully for user {}. Cart now has {} items.", productId, userId, cart.getCartItems().size());

        return CartMapper.convertToResponseDto(cart);
    }


    /**
     * Removes an item from the cart.
     */
    @Override
    @Transactional
    public boolean removeFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        Optional<CartItem> cartItem = cartItemRepository.findByCartAndProductId(cart, productId);

        if (cartItem.isEmpty()) {
            log.warn("Cart item not found for user {} and product {}", userId, productId);
            return false;
        }

        log.info("Deleting cartItem: {}", cartItem.get().getId());
        cartItemRepository.delete(cartItem.get());
        return true;
    }



    /**
     * Clears all items from the cart.
     */
    @Override
    @Transactional
    public void clearCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for user: " + userId));

        cartItemRepository.deleteByCartId(cart.getId());
        log.info("Cleared cart for user {}", userId);
    }

    @Override
    @Transactional
    public CartResponseDto updateCartItem(Long userId, Long productId, int quantity) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for user ID: " + userId));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found for product ID: " + productId));

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return CartMapper.convertToResponseDto(cart);
    }

    /**
     * Creates a new cart for a user.
     */
    private Cart createCartForUser(User user) {
        Cart cart = new Cart(user);
        return cartRepository.save(cart);
    }

    /**
     * Creates a new cart for a user ID.
     */
    private Cart createCartForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return createCartForUser(user);
    }

    public boolean isCartEmpty(Long userId) {
        return cartRepository.findByUserId(userId)
                .map(cart -> cart.getCartItems().isEmpty())
                .orElse(true);
    }

}
