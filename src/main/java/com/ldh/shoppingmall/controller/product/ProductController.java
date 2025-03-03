package com.ldh.shoppingmall.controller.product;

import com.ldh.shoppingmall.entity.product.Product;
import com.ldh.shoppingmall.service.product.ProductService;
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

    private final ProductService productService;

    @GetMapping("/{productId}")
    public String showProductDetail(@PathVariable Long productId,
                                    Model model) {

        Product product = productService.findById(productId);

        if (product == null) return "error/404";

        model.addAttribute("product", product);
        return "product/detail";
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> removeProduct(@PathVariable Long productId) {
        productService.removeProduct(productId);
        return ResponseEntity.ok("Product deleted successfully.");
    }
}
