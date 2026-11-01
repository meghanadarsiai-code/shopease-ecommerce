package com.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, java.util.NoSuchElementException.class})
    public String notFound(Exception e, Model model) {
        model.addAttribute("message", e.getMessage() == null ? "Not found" : e.getMessage());
        return "error-page";
    }
}
