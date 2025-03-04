package com.ldh.shoppingmall.controller.cart;

import com.ldh.shoppingmall.dto.cart.CartResponseDto;
import com.ldh.shoppingmall.service.cart.CartService;
import com.ldh.shoppingmall.service.cart.GuestCartService;
import com.ldh.shoppingmall.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;
    private final GuestCartService guestCartService;
    private final UserService userService;

    /**
     * Displays the shopping cart page (supports both logged-in users and guests)
     */
    @GetMapping
    public String viewCart(Model model) {
        Optional<Long> userId = getCurrentUserId();

        log.info("Viewing cart for userId: {}", userId.orElse(null));

        CartResponseDto cart = userId.map(cartService::getCartByUserId)
                .orElseGet(guestCartService::getCartByUserId);

        model.addAttribute("cart", cart);
        return "cart/cart";
    }

    /**
     * Removes an item from the cart (only for logged-in users)
     */
    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long cartItemId, RedirectAttributes redirectAttributes) {
        Optional<Long> userId = getCurrentUserId();

        if (userId.isEmpty()) {
            log.warn("Guest users cannot remove specific items from the cart.");
            redirectAttributes.addFlashAttribute("error", "You must be logged in to remove items.");
            return "redirect:/cart";
        }

        log.info("Removing item {} from user {}'s cart", cartItemId, userId.get());
        cartService.removeFromCart(userId.get(), cartItemId);
        redirectAttributes.addFlashAttribute("message", "Item removed successfully");

        return "redirect:/cart";
    }

    /**
     * Clears all items from the cart (supports both logged-in users and guests)
     */
    @PostMapping("/clear")
    public String clearCart(RedirectAttributes redirectAttributes) {
        Optional<Long> userId = getCurrentUserId();

        if (userId.isPresent()) {
            log.info("Clearing cart for user {}", userId.get());
            cartService.clearCart(userId.get());
        } else {
            log.info("Clearing guest cart");
            guestCartService.clearCart();
        }

        redirectAttributes.addFlashAttribute("message", "Cart cleared successfully");
        return "redirect:/cart";
    }

    /**
     * Retrieves the currently logged-in user's ID (returns Optional)
     */
    private Optional<Long> getCurrentUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            log.info("Authenticated user: {}", userDetails.getUsername()); // check username
            Long userId = userService.findUserIdByUsername(userDetails);
            log.info("Retrieved user ID: {}", userId); // check userId

            return Optional.ofNullable(userId);
        }

        log.warn("No authenticated user found (Guest user)");
        return Optional.empty(); // Guest user
    }
}
