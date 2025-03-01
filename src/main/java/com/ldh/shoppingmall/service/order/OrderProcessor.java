package com.ldh.shoppingmall.service.order;

import com.ldh.shoppingmall.entity.order.Order;

public interface OrderProcessor {
    void process(Order order);
}
