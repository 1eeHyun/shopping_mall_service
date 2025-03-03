package com.ldh.shoppingmall.api;

import com.ldh.shoppingmall.dto.cart.CartResponseDto;
import com.ldh.shoppingmall.service.cart.CartService;
import com.ldh.shoppingmall.service.cart.GuestCartService;
import com.ldh.shoppingmall.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartRestController {

    private final CartService cartService;
    private final GuestCartService guestCartService;
    private final UserService userService;

    /** Get cart */
    @GetMapping
    public ResponseEntity<CartResponseDto> getCart() {
        return getCurrentUserId()
                .map(cartService::getCartByUserId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(guestCartService.getCartByUserId()));
    }

    /** Add a product to cart */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addToCart(@RequestBody Map<String, Object> request) {

        Long productId = parseLong(request.get("productId"));
        Integer quantity = parseInt(request.get("quantity"), 1);

        if (productId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Product ID is required"));
        }

        getCurrentUserId().ifPresentOrElse(
                userId -> cartService.addToCart(userId, productId, quantity),
                () -> guestCartService.addToCart(productId, quantity)
        );

        return ResponseEntity.ok(Map.of("message", "Product added to cart"));
    }

    /** Update quantity */
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateCart(@RequestBody Map<String, Object> request) {

        Long productId = parseLong(request.get("productId"));
        Integer quantity = parseInt(request.get("quantity"), null);

        if (productId == null || quantity == null || quantity < 1) {
            return ResponseEntity.badRequest().body(Map.of("error", "Valid product ID and quantity are required"));
        }

        getCurrentUserId().ifPresentOrElse(
                userId -> cartService.updateCartItem(userId, productId, quantity),
                () -> guestCartService.updateCartItem(productId, quantity)
        );

        return ResponseEntity.ok(Map.of("message", "Cart updated successfully", "redirectUrl", "/cart"));
    }

    /** Remove an item from the cart */
    @DeleteMapping("/remove")
    public ResponseEntity<Map<String, Object>> removeFromCart(@RequestParam Long productId) {
        Optional<Long> userId = getCurrentUserId();

        log.info("Received remove request for productId: {}", productId);

        if (userId.isPresent()) {
            log.info("User ID: {}", userId.get());

            boolean removed = cartService.removeFromCart(userId.get(), productId);
            if (!removed) {
                log.warn("⚠ Cart item not found for productId: {}", productId);
                return ResponseEntity.status(404).body(Map.of("error", "Cart item not found"));
            }

            log.info("Removed product {} from user {}'s cart", productId, userId.get());

            if (cartService.isCartEmpty(userId.get())) {
                log.info("🛒 Cart is now empty for user {}", userId.get());
                return ResponseEntity.ok(Map.of("message", "Cart is now empty", "redirectUrl", "/cart"));
            }

            return ResponseEntity.ok(Map.of("message", "Item removed successfully"));
        } else {
            log.info("Guest user - removing product {}", productId);
            guestCartService.removeFromCart(productId);
        }

        return ResponseEntity.ok(Map.of("message", "Item removed successfully"));
    }


    /** Clear a cart */
    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearCart() {

        getCurrentUserId().ifPresentOrElse(
                cartService::clearCart,
                guestCartService::clearCart
        );

        return ResponseEntity.ok(Map.of("message", "Cart cleared"));
    }

    /** Get logged-in id */
    private Optional<Long> getCurrentUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return Optional.ofNullable(userService.findUserIdByUsername(userDetails));
        }

        return Optional.empty();
    }

    /** ParseLong */
    private Long parseLong(Object value) {
        try {
            return value != null ? Long.valueOf(value.toString()) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** String -> Integer */
    private Integer parseInt(Object value, Integer defaultValue) {
        try {
            return value != null ? Integer.valueOf(value.toString()) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
