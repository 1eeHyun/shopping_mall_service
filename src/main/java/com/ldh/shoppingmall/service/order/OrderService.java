package com.ldh.shoppingmall.service.order;

import com.ldh.shoppingmall.dto.order.OrderResponseDto;
import com.ldh.shoppingmall.entity.order.Order;
import com.ldh.shoppingmall.entity.order.OrderItem;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(Long userId, List<OrderItem> orderItems);
    OrderResponseDto getOrderById(Long orderId);
    List<OrderResponseDto> getOrderByUser(Long userId);
    void cancelOrder(Long orderId);
    void updateOrderStatus(Long orderId, Order.OrderStatus status);
    OrderResponseDto convertToDto(Order order);
}
