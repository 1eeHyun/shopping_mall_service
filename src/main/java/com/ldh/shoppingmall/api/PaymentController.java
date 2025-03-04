package com.ldh.shoppingmall.api;

import com.ldh.shoppingmall.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/paypal")
    public String createPayPalOrder(@RequestParam Long userId) {
        return paymentService.createOrder(userId);
    }

    @GetMapping("/credit-card")
    public String createCreditCardOrder(@RequestParam double amount) {

        // credit card payment logic
        return "home";
    }

    @GetMapping("/bank-transfer")
    public String createBankTransferOrder(@RequestParam double amount) {

        // Bank Transfer payment logic
        return "home";
    }
}
