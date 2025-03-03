package com.ldh.shoppingmall.service.cart;

import com.ldh.shoppingmall.dto.cart.CartItemDto;
import com.ldh.shoppingmall.dto.cart.CartResponseDto;
import com.ldh.shoppingmall.entity.product.Product;
import com.ldh.shoppingmall.mapper.CartMapper;
import com.ldh.shoppingmall.repository.product.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.ldh.shoppingmall.mapper.CartMapper.convertGuestCartToDto;

@Slf4j
@Service
@SessionScope
@RequiredArgsConstructor
public class GuestCartServiceImpl implements GuestCartService {

    private final ProductRepository productRepository;
    private final Map<Long, CartItemDto> cart = new ConcurrentHashMap<>();

    @Override
    public CartResponseDto getCartByUserId() {
        log.info("Returning guest session cart");
        return convertGuestCartToDto(cart);
    }

    @Override
    public CartResponseDto addToCart(Long productId, int quantity) {

        log.info("🛒 Adding product {} to guest cart", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("🚨 Product not found with ID: " + productId));

        cart.compute(productId, (key, existingItem) -> {
            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                existingItem.setTotalPrice(existingItem.getPrice().multiply(BigDecimal.valueOf(existingItem.getQuantity())));
                return existingItem;
            } else {
                return new CartItemDto(
                        productId,
                        product.getProductName(),
                        quantity,
                        product.getPrice()
                );
            }
        });

        return CartMapper.convertGuestCartToDto(cart);
    }

    @Override
    public void removeFromCart(Long productId) {
        log.info("Removing product {} from guest cart", productId);
        cart.remove(productId);
    }

    @Override
    public void clearCart() {
        log.info("Clearing guest cart");
        cart.clear();
    }

    @Override
    public CartResponseDto updateCartItem(Long productId, int quantity) {
        log.info("Updating guest cart item: Product ID = {}, New Quantity = {}", productId, quantity);

        CartItemDto item = cart.get(productId);
        if (item == null) {
            throw new IllegalArgumentException("Product ID " + productId + " not found in guest cart");
        }

        item.setQuantity(quantity);
        item.setTotalPrice(item.getPrice().multiply(BigDecimal.valueOf(quantity)));

        return CartMapper.convertGuestCartToDto(cart);
    }
}
