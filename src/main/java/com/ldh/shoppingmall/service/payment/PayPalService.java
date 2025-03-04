package com.ldh.shoppingmall.service.payment;

import com.ldh.shoppingmall.service.cart.CartService;
import com.paypal.core.PayPalHttpClient;
import com.paypal.orders.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayPalService implements PaymentService {

    private final PayPalHttpClient payPalHttpClient;
    private final CartService cartService;

    @Override
    public String createOrder(Long userId) {

        // get total amount in a user's cart
        BigDecimal totalAmount = cartService.getCartTotalAmount(userId);

        if (totalAmount.compareTo(BigDecimal.ZERO) == 0) {
            return "The cart is empty.";
        }

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        AmountWithBreakdown amount = new AmountWithBreakdown()
                .currencyCode("USD")
                .value(totalAmount.toString());

        PurchaseUnitRequest purchaseUnit = new PurchaseUnitRequest().amountWithBreakdown(amount);

        List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
        purchaseUnits.add(purchaseUnit);
        orderRequest.purchaseUnits(purchaseUnits);

        OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);

        try {
            Order order = payPalHttpClient.execute(request).result();
            for (LinkDescription link : order.links()) {
                if ("approve".equals(link.rel())) {
                    return link.href(); // Return PayPal payment approval URL
                }
            }
            return "Cannot find approval URL";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to create order";
        }
    }
}
