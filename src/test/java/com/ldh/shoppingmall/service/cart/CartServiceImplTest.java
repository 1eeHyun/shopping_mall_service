package com.ldh.shoppingmall.service.cart;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks private CartServiceImpl cartService;

    private User testUser;
    private Product testProduct;
    private Cart testCart;
    private CartItem testCartItem;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setProductName("Test Product");
        testProduct.setPrice(new BigDecimal("100.00"));

        testCart = new Cart(testUser);
        testCart.setId(1L);

        testCartItem = new CartItem(testCart, testProduct, 1, testProduct.getPrice());
        testCartItem.setId(1L);
        testCart.setCartItems(new ArrayList<>(List.of(testCartItem)));

        lenient().when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        lenient().when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
    }




    /**
     * Retrieve cart
     */
    @Test
    @DisplayName("Get Cart By UserId - Success")
    void testGetCartByUserId_Success() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));

        CartResponseDto response = cartService.getCartByUserId(1L);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertEquals(1, response.getCartItems().size());
        assertEquals("Test Product", response.getCartItems().get(0).getProductName());

        verify(cartRepository, times(1)).findByUserId(1L);
    }

    @Test
    @DisplayName("Get Cart By UserId - Empty Cart")
    void testGetCartByUserId_EmptyCart() {

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(cartRepository.save(any(Cart.class))).thenReturn(new Cart(testUser));

        CartResponseDto response = cartService.getCartByUserId(1L);

        assertNotNull(response);
        assertTrue(response.getCartItems().isEmpty());

        verify(cartRepository, times(1)).findByUserId(1L);
    }


    /**
     * Add product to cart
     */
    @Test
    @DisplayName("Add Product to Cart - Success")
    void testAddToCart_Success() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));

        testCart.setCartItems(new ArrayList<>());

        CartResponseDto response = cartService.addToCart(1L, 1L, 1);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertEquals(1, response.getCartItems().size());

        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    @DisplayName("Add Product to Cart - Product Not Found")
    void testAddToCart_ProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cartService.addToCart(1L, 1L, 1));

        verify(productRepository, times(1)).findById(1L);
    }

    /**
     * Remove product from cart
     */
    @Test
    @DisplayName("Remove Item from Cart - Success")
    void testRemoveFromCart_Success() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));
        when(cartItemRepository.findByCartAndProductId(testCart, 1L)).thenReturn(Optional.of(testCartItem));

        boolean result = cartService.removeFromCart(1L, 1L);

        assertTrue(result);
        verify(cartItemRepository, times(1)).delete(testCartItem);
    }

    @Test
    @DisplayName("Remove Item from Cart - Item Not Found")
    void testRemoveFromCart_ItemNotFound() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));
        when(cartItemRepository.findByCartAndProductId(testCart, 1L)).thenReturn(Optional.empty());

        boolean result = cartService.removeFromCart(1L, 1L);

        assertFalse(result);
        verify(cartItemRepository, never()).delete(any());
    }

    /**
     * Empty cart
     */
    @Test
    @DisplayName("Clear Cart - Success")
    void testClearCart_Success() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));

        cartService.clearCart(1L);

        verify(cartItemRepository, times(1)).deleteByCartId(1L);
    }

    @Test
    @DisplayName("Clear Cart - Cart Not Found")
    void testClearCart_CartNotFound() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cartService.clearCart(1L));

        verify(cartRepository, times(1)).findByUserId(1L);
    }

    /**
     * Update cart
     */
    @Test
    @DisplayName("Update Cart Item - Success")
    void testUpdateCartItem_Success() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));

        CartResponseDto response = cartService.updateCartItem(1L, 1L, 3);

        assertEquals(3, testCartItem.getQuantity());
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    @DisplayName("Update Cart Item - Cart Not Found")
    void testUpdateCartItem_CartNotFound() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cartService.updateCartItem(1L, 1L, 3));

        verify(cartRepository, times(1)).findByUserId(1L);
    }
}
