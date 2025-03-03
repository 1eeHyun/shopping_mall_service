package com.ldh.shoppingmall.controller.user;

import com.ldh.shoppingmall.dto.user.UserDto;
import com.ldh.shoppingmall.service.user.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserServiceImpl userServiceImpl;

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login"; // Login page
    }

    @GetMapping("/signup")
    public String showSignupForm() {
        return "auth/signup"; // signup page
    }

    @PostMapping("/signup")
    public String register(@Valid @ModelAttribute UserDto userDto, Model model) {

        try {
            userServiceImpl.register(userDto);
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/signup";
        }
    }

}
