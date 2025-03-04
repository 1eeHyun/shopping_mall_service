package com.ldh.shoppingmall.controller;

import com.ldh.shoppingmall.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addUserIdToModel(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 Model model) {

        if (userDetails != null)
            model.addAttribute("userId", userDetails.getId());
    }
}
