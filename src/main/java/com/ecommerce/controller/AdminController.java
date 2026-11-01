package com.ecommerce.controller;

import com.ecommerce.model.Product;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductRepository products;
    private final CartItemRepository cartItems;
    private final OrderService orderService;

    @GetMapping("/products")
    public String list(Model model) {
        model.addAttribute("products", products.findAll());
        return "admin/products";
    }

    @GetMapping("/products/new")
    public String newForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/product-form";
    }

    @GetMapping("/products/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("product", products.findById(id).orElseThrow());
        return "admin/product-form";
    }

    @PostMapping("/products/save")
    public String save(@Valid @ModelAttribute("product") Product product, BindingResult result,
                       RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "admin/product-form";
        }
        products.save(product);
        ra.addFlashAttribute("success", "Product saved");
        return "redirect:/admin/products";
    }

    @PostMapping("/products/{id}/delete")
    @Transactional
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        products.findById(id).ifPresent(p -> {
            cartItems.deleteByProduct(p);
            products.delete(p);
        });
        ra.addFlashAttribute("success", "Product deleted");
        return "redirect:/admin/products";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("orders", orderService.all());
        return "admin/orders";
    }

    @PostMapping("/orders/{id}/status")
    public String status(@PathVariable Long id, @RequestParam String status, RedirectAttributes ra) {
        orderService.updateStatus(id, status);
        ra.addFlashAttribute("success", "Order #" + id + " marked " + status);
        return "redirect:/admin/orders";
    }
}
