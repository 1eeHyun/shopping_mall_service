package com.ldh.shoppingmall.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public String handleResponseStatusException(ResponseStatusException e, Model model) {
        model.addAttribute("error", e.getReason());
        return "auth/signup"; // Exception -> log-in page
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception e, Model model) {
        e.printStackTrace(); // Check log
        model.addAttribute("error", "An unexpected error occurred.");
        return "auth/signup";
    }
}

