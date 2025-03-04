package com.ldh.shoppingmall.controller;

import com.ldh.shoppingmall.entity.product.Product;
import com.ldh.shoppingmall.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;

    @GetMapping
    public String showHome(@RequestParam(name = "query", required = false) String query,
                           Principal principal,
                           Model model){

        if (principal != null) {
            model.addAttribute("username", principal.getName()); // Logged-in user name
        }

        if (query != null && !query.isEmpty()) {
            List<Product> searchResults = productService.searchProductsByName(query);
            model.addAttribute("title", "Search Results");
            model.addAttribute("products", searchResults);
        } else {
            List<Product> recommendedProducts = productService.getRecommendedProducts(4);
            model.addAttribute("title", "Recommended Products");
            model.addAttribute("products", recommendedProducts);
        }

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
