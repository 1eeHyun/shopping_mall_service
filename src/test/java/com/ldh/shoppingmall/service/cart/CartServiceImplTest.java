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

    @InjectMocks
    private CartServiceImpl cartService;

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
        testCart.setCartItems(List.of(testCartItem));
    }

    /**
     * Test for retrieving cart
     */
    @Test
    @DisplayName("Get Cart By UserId Success")
    void testGetCartByUserId_Success() {

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));

        var cartDto = cartService.getCartByUserId(1L);

        assertNotNull(cartDto);
        assertEquals(1L, cartDto.getUserId());
        assertEquals(1, cartDto.getCartItems().size());
        assertEquals("Test Product", cartDto.getCartItems().get(0).getProductName());

        verify(cartRepository, times(1)).findByUserId(1L);
    }

    @Test
    @DisplayName("Get Cart By UserId Fail")
    void testGetCartByUserId_NotFound() {

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cartService.getCartByUserId(1L));

        verify(cartRepository, times(1)).findByUserId(1L);
    }

    /**
     * Add product to a cart test
     */
    @Test
    @DisplayName("Add Product Success")
    void testAddToCart_Success() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(testCartItem);

        CartResponseDto response = cartService.addToCart(1L, 1L, 1);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertEquals(1, response.getCartItems().size());

        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    @DisplayName("Add Product User Not Found Fail")
    void testAddToCart_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cartService.addToCart(1L, 1L, 1));

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Add Product Product Not Found Fail")
    void testAddToCart_ProductNotFound() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cartService.addToCart(1L, 1L, 1));

        verify(productRepository, times(1)).findById(1L);
    }

    /**
     * Remove an item from a cart
     */
    @Test
    @DisplayName("Remove Item From Cart Success")
    void testRemoveFromCart_Success() {

        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(testCartItem));

        cartService.removeFromCart(1L);

        verify(cartItemRepository, times(1)).delete(testCartItem);
    }

    @Test
    @DisplayName("Remove Item From Cart Fail")
    void testRemoveFromCart_ItemNotFound() {
        when(cartItemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cartService.removeFromCart(1L));

        verify(cartItemRepository, times(1)).findById(1L);
    }

    /**
     * Clear cart
     */
    @Test
    @DisplayName("Clear Cart Success")
    void testClearCart_Success() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));

        cartService.clearCart(1L);

        verify(cartItemRepository, times(1)).deleteAll(anyList());
        verify(cartRepository, times(1)).save(testCart);
    }

    @Test
    @DisplayName("Clear Cart Fail")
    void testClearCart_CartNotFound() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cartService.clearCart(1L));

        verify(cartRepository, times(1)).findByUserId(1L);
    }
}