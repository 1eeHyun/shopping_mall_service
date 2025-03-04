package com.ldh.shoppingmall.service.payment;

import com.ldh.shoppingmall.service.cart.CartService;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.LinkDescription;
import com.paypal.orders.Order;
import com.paypal.orders.OrdersCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PayPalServiceTest {

    @Mock
    private PayPalHttpClient payPalHttpClient;

    @Mock
    private CartService cartService;

    @InjectMocks
    private PayPalService payPalService;

    private final Long testUserId = 1L;

    @BeforeEach
    void setUp() {

        Mockito.lenient().when(cartService.getCartTotalAmount(testUserId))
                .thenReturn(new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("CreateOrder Success")
    void createOrder_ShouldReturnPayPalApprovalUrl() throws IOException {

        // Given
        Order mockOrder = mock(Order.class);
        LinkDescription mockLink = new LinkDescription();
        mockLink.rel("approve");
        mockLink.href("https://www.sandbox.paypal.com/checkoutnow?token=TEST_TOKEN");

        HttpResponse<Order> mockResponse = mock(HttpResponse.class);

        when(mockResponse.result()).thenReturn(mockOrder);
        when(mockOrder.links()).thenReturn(Collections.singletonList(mockLink));
        when(payPalHttpClient.execute(any(OrdersCreateRequest.class))).thenReturn(mockResponse);

        // When
        String approvalUrl = payPalService.createOrder(testUserId);

        // Then
        assertNotNull(approvalUrl);
        assertTrue(approvalUrl.contains("https://www.sandbox.paypal.com/checkoutnow"));
    }

    @Test
    @DisplayName("CreateOrder Fail Error Message Cart is Empty")
    void createOrder_ShouldReturnErrorMessage_WhenCartIsEmpty() {
        // Given
        when(cartService.getCartTotalAmount(testUserId)).thenReturn(BigDecimal.ZERO);

        // When
        String response = payPalService.createOrder(testUserId);

        // Then
        assertEquals("The cart is empty.", response);
    }

    @Test
    @DisplayName("CreateOrder Fail Error Message")
    void createOrder_ShouldReturnErrorMessage_WhenPayPalFails() throws IOException {
        // Given
        when(payPalHttpClient.execute(any(OrdersCreateRequest.class))).thenThrow(new IOException("PayPal API Error"));

        // When
        String response = payPalService.createOrder(testUserId);

        // Then
        assertEquals("Failed to create order", response);
    }
}