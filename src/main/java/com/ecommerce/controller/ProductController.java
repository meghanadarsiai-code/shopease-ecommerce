package com.ecommerce.controller;

import com.ecommerce.model.Product;
import com.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository products;

    @GetMapping("/")
    public String home(@RequestParam(defaultValue = "") String q,
                       @RequestParam(required = false) String category,
                       Model model) {
        List<Product> list = (category == null || category.isBlank())
                ? products.findByNameContainingIgnoreCase(q)
                : products.findByNameContainingIgnoreCaseAndCategory(q, category);
        model.addAttribute("products", list);
        model.addAttribute("categories", products.findCategories());
        model.addAttribute("q", q);
        model.addAttribute("selectedCategory", category);
        return "index";
    }

    @GetMapping("/products/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Product p = products.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        model.addAttribute("product", p);
        return "product";
    }
}
