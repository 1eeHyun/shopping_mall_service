package com.ldh.shoppingmall.controller.product;

import com.ldh.shoppingmall.service.product.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductServiceImpl productServiceImpl;

    @GetMapping("/{productId}")
    public String showProductDetail(@PathVariable Long productId,
                                    Model model) {

        return "products/" + productId;
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<String> removeProduct(@PathVariable Long productId) {
        productServiceImpl.removeProduct(productId);
        return ResponseEntity.ok("Product deleted successfully.");
    }
}
