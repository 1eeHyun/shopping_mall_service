package com.ldh.shoppingmall.entity.order;

import com.ldh.shoppingmall.entity.BaseEntity;
import com.ldh.shoppingmall.entity.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems; // order item list

    public Order(String orderNumber,
                 BigDecimal totalPrice,
                 OrderStatus status,
                 User user,
                 List<OrderItem> orderItems) {
        this.orderNumber = orderNumber;
        this.totalPrice = totalPrice;
        this.status = status;
        this.user = user;
        this.orderItems = orderItems;
    }

    public enum OrderStatus {
        PENDING,   // wait for payment
        PAID,      // payment completed
        SHIPPED,   // delivering
        DELIVERED, // delivered
        CANCELED  // order canceled
    }
}
