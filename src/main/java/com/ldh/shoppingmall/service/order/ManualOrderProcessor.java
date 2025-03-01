package com.ldh.shoppingmall.service.order;

import com.ldh.shoppingmall.entity.order.Order;
import org.springframework.stereotype.Service;

import static com.ldh.shoppingmall.entity.order.Order.*;

@Service
public class ManualOrderProcessor implements OrderProcessor{

    @Override
    public void process(Order order) {
        order.setStatus(OrderStatus.PENDING);
    }
}
