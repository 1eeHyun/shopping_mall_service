package com.ldh.shoppingmall.controller;

import com.ldh.shoppingmall.entity.product.Product;
import com.ldh.shoppingmall.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;

    @GetMapping
    public String showHome(Principal principal,
                           Model model){

        if (principal != null) {
            model.addAttribute("username", principal.getName()); // Logged-in user name
        }

        List<Product> recommendedProducts = productService.getRecommendedProducts(4);
        model.addAttribute("recommendedProducts", recommendedProducts);
        return "home";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "auth/login";
    }

    @GetMapping("/signup")
    public String showSignUp() {
        return "auth/signup";
    }
}
