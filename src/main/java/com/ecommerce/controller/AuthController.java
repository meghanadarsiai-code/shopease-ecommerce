package com.ecommerce.controller;

import com.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password,
                           Model model,
                           RedirectAttributes ra) {
        if (name.isBlank() || email.isBlank() || password.length() < 6) {
            model.addAttribute("error", "Fill all fields; password must be at least 6 characters");
            return "register";
        }
        try {
            userService.register(name, email, password);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
        ra.addFlashAttribute("success", "Account created! Please log in.");
        return "redirect:/login";
    }
}
