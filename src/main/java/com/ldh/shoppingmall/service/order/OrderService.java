package com.ldh.shoppingmall.service.order;

import com.ldh.shoppingmall.dto.OrderItemDto;
import com.ldh.shoppingmall.dto.OrderResponseDto;
import com.ldh.shoppingmall.entity.order.Order;
import com.ldh.shoppingmall.entity.order.OrderItem;
import com.ldh.shoppingmall.entity.user.User;
import com.ldh.shoppingmall.repository.OrderRepository;
import com.ldh.shoppingmall.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.ldh.shoppingmall.entity.order.Order.OrderStatus;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    /**
     * Create order
     */
    @Transactional
    public OrderResponseDto createOrder(Long userId, List<OrderItem> orderItems) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Get total money
        BigDecimal totalPrice = calculateTotalPrice(orderItems);

        // Create Order object
        Order order = new Order(
                generateOrderNumber(),
                totalPrice,
                OrderStatus.PENDING,
                user,
                orderItems
        );

        // Set OrderItem
        orderItems.forEach(item -> item.setOrder(order));

        Order savedOrder = orderRepository.save(order);
        return convertToDto(savedOrder);
    }

    /**
     * Retrieve an order
     */
    public OrderResponseDto getOrderById(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return convertToDto(order);
    }

    /**
     * Retrieve every order of a certain user
     */
    public List<OrderResponseDto> getOrderByUser(Long userId) {

        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Cancel an order
     */
    @Transactional
    public void cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    /**
     * Change order status (delivering, delivered)
     */
    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        order.setStatus(status);
        orderRepository.save(order);
    }

    /**
     * Convert Order entity to its dto
     */
    public OrderResponseDto convertToDto(Order order) {

        OrderResponseDto dto = new OrderResponseDto();
        dto.setOrderId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setStatus(order.getStatus());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setCreatedAt(order.getCreatedAt());

        // Convert order item to dto
        List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                .map(orderItem -> {
                    OrderItemDto itemDto = new OrderItemDto();
                    itemDto.setProductId(orderItem.getProduct().getId());
                    itemDto.setProductName(orderItem.getProduct().getProductName());
                    itemDto.setQuantity(orderItem.getQuantity());
                    itemDto.setPrice(orderItem.getPrice());
                    return itemDto;

                }).collect(Collectors.toList());

        dto.setOrderItems(orderItemDtos);
        return dto;
    }

    /**
     * calculate total price
     */
    private BigDecimal calculateTotalPrice(List<OrderItem> orderItems) {

        return orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * create random order number
     */
    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis();
    }
}
