package com.ecommerce.controller;

import com.ecommerce.model.CustomerOrder;
import com.ecommerce.model.User;
import com.ecommerce.service.CartService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final UserService userService;

    @GetMapping("/checkout")
    public String checkout(Principal principal, Model model, RedirectAttributes ra) {
        User user = userService.getByEmail(principal.getName());
        if (cartService.items(user).isEmpty()) {
            ra.addFlashAttribute("error", "Your cart is empty");
            return "redirect:/cart";
        }
        model.addAttribute("items", cartService.items(user));
        model.addAttribute("total", cartService.total(user));
        return "checkout";
    }

    @PostMapping("/checkout")
    public String place(@RequestParam String address, Principal principal, RedirectAttributes ra) {
        if (address == null || address.isBlank()) {
            ra.addFlashAttribute("error", "Shipping address is required");
            return "redirect:/checkout";
        }
        try {
            CustomerOrder order = orderService.place(userService.getByEmail(principal.getName()), address.trim());
            ra.addFlashAttribute("success", "Order #" + order.getId() + " placed successfully!");
            return "redirect:/orders/" + order.getId();
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart";
        }
    }

    @GetMapping("/orders")
    public String list(Principal principal, Model model) {
        model.addAttribute("orders", orderService.forUser(userService.getByEmail(principal.getName())));
        return "orders";
    }

    @GetMapping("/orders/{id}")
    public String detail(@PathVariable Long id, Principal principal, Model model) {
        User user = userService.getByEmail(principal.getName());
        CustomerOrder order = orderService.get(id);
        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Order not found");
        }
        model.addAttribute("order", order);
        return "order-detail";
    }
}
