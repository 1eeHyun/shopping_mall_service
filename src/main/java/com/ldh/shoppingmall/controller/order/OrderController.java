package com.ldh.shoppingmall.controller.order;

import com.ldh.shoppingmall.dto.order.OrderResponseDto;
import com.ldh.shoppingmall.service.order.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ldh.shoppingmall.entity.order.Order.OrderStatus;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderServiceImpl orderServiceImpl;

    @GetMapping("/{orderId}")
    public OrderResponseDto getOrder(@PathVariable Long orderId) {
        return orderServiceImpl.getOrderById(orderId);
    }

    @GetMapping("/user/{userId}")
    public List<OrderResponseDto> getUserOrders(@PathVariable Long userId) {
        return orderServiceImpl.getOrderByUser(userId);
    }

    @PostMapping("/{orderId}/cancel")
    public void cancelOrder(@PathVariable Long orderId) {
        orderServiceImpl.cancelOrder(orderId);
    }

    @PutMapping("/{orderId}/status/{status}")
    public void updateOrderStatus(@PathVariable Long orderId,
                                  @PathVariable String status) {

        orderServiceImpl.updateOrderStatus(orderId, OrderStatus.valueOf(status));
    }
}
