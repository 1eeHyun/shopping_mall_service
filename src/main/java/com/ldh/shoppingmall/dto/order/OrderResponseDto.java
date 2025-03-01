package com.ldh.shoppingmall.dto.order;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.ldh.shoppingmall.entity.order.Order.OrderStatus;

@Getter
@Setter
public class OrderResponseDto {

    private Long orderId;
    private String orderNumber;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private List<OrderItemDto> orderItems;
}
