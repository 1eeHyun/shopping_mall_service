package com.ldh.shoppingmall.service.order;


import com.ldh.shoppingmall.dto.order.OrderResponseDto;
import com.ldh.shoppingmall.entity.order.Order;
import com.ldh.shoppingmall.entity.order.OrderItem;
import com.ldh.shoppingmall.entity.product.Product;
import com.ldh.shoppingmall.entity.user.User;
import com.ldh.shoppingmall.repository.order.OrderRepository;
import com.ldh.shoppingmall.repository.user.UserRepository;
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

import static com.ldh.shoppingmall.entity.order.Order.OrderStatus;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    private User testUser;
    private Order testOrder;
    private List<OrderItem> testOrderItems;

    @BeforeEach
    void setUp() {

        // Set user data
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        // Set product data
        Product product1 = new Product();
        product1.setId(1L);
        product1.setProductName("product 1");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setProductName("product 2");

        // Set order item
        OrderItem item1 = new OrderItem();
        item1.setId(1L);
        item1.setQuantity(2);
        item1.setPrice(new BigDecimal("50.00"));
        item1.setProduct(product1);

        OrderItem item2 = new OrderItem();
        item1.setId(2L);
        item2.setQuantity(1);
        item2.setPrice(new BigDecimal("100.00"));
        item2.setProduct(product2);

        testOrderItems = List.of(item1, item2);

        // Set order
        testOrder = new Order(
                "ORD-123456",
                new BigDecimal("200.00"),
                OrderStatus.PENDING,
                testUser,
                testOrderItems
        );
    }

    @Test
    @DisplayName("Create Order Success")
    void Create_Order_Success() {

        // Given
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        // When
        OrderResponseDto result = orderServiceImpl.createOrder(testUser.getId(), testOrderItems);

        // Then
        assertNotNull(result);
        assertEquals("ORD-123456", result.getOrderNumber());
        assertEquals(OrderStatus.PENDING, result.getStatus());
        assertEquals(new BigDecimal("200.00"), result.getTotalPrice());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("Get Order By ID Success")
    void Get_OrderById_Success() {

        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        // When
        OrderResponseDto result = orderServiceImpl.getOrderById(1L);

        // Then
        assertNotNull(result);
        assertEquals("ORD-123456", result.getOrderNumber());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get Order By ID Fail")
    void GET_OrderByID_NotFound() {

        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // When and Then
        assertThrows(RuntimeException.class, () -> orderServiceImpl.getOrderById(1L));
    }

    @Test
    @DisplayName("Cancel Order Success")
    void Cancel_Order_Success() {

        // Given
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        // When
        orderServiceImpl.cancelOrder(1L);

        // Then
        assertEquals(OrderStatus.CANCELED, testOrder.getStatus());
        verify(orderRepository, times(1)).save(testOrder);
    }
}
