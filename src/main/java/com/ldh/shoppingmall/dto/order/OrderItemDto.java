package com.ldh.shoppingmall.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemDto {

    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal price;
}
