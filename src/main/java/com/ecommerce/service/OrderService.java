package com.ecommerce.service;

import com.ecommerce.model.*;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orders;
    private final CartItemRepository cart;
    private final ProductRepository products;

    @Transactional
    public CustomerOrder place(User user, String address) {
        List<CartItem> items = cart.findByUser(user);
        if (items.isEmpty()) {
            throw new IllegalStateException("Your cart is empty");
        }
        CustomerOrder order = new CustomerOrder();
        order.setUser(user);
        order.setShippingAddress(address);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("PLACED");

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem ci : items) {
            Product p = ci.getProduct();
            if (ci.getQuantity() > p.getStock()) {
                throw new IllegalStateException("Not enough stock for " + p.getName());
            }
            p.setStock(p.getStock() - ci.getQuantity());
            products.save(p);

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProductName(p.getName());
            oi.setUnitPrice(p.getPrice());
            oi.setQuantity(ci.getQuantity());
            order.getItems().add(oi);
            total = total.add(oi.getSubtotal());
        }
        order.setTotal(total);
        orders.save(order);
        cart.deleteAll(items);
        return order;
    }

    public List<CustomerOrder> forUser(User user) {
        return orders.findByUserOrderByCreatedAtDesc(user);
    }

    public List<CustomerOrder> all() {
        return orders.findAllByOrderByCreatedAtDesc();
    }

    public CustomerOrder get(Long id) {
        return orders.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    @Transactional
    public void updateStatus(Long id, String status) {
        CustomerOrder o = get(id);
        o.setStatus(status);
        orders.save(o);
    }
}
