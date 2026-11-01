package com.ecommerce.service;

import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cart;
    private final ProductRepository products;

    public List<CartItem> items(User user) {
        return cart.findByUser(user);
    }

    public BigDecimal total(User user) {
        return items(user).stream().map(CartItem::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int count(User user) {
        return items(user).stream().mapToInt(CartItem::getQuantity).sum();
    }

    @Transactional
    public void add(User user, Long productId, int qty) {
        Product p = products.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        if (qty < 1) qty = 1;
        CartItem item = cart.findByUserAndProduct(user, p).orElseGet(() -> {
            CartItem c = new CartItem();
            c.setUser(user);
            c.setProduct(p);
            c.setQuantity(0);
            return c;
        });
        int newQty = item.getQuantity() + qty;
        if (newQty > p.getStock()) {
            throw new IllegalStateException("Only " + p.getStock() + " unit(s) of " + p.getName() + " available");
        }
        item.setQuantity(newQty);
        cart.save(item);
    }

    @Transactional
    public void update(User user, Long itemId, int qty) {
        CartItem item = owned(user, itemId);
        if (qty <= 0) {
            cart.delete(item);
            return;
        }
        if (qty > item.getProduct().getStock()) {
            throw new IllegalStateException("Only " + item.getProduct().getStock() + " unit(s) available");
        }
        item.setQuantity(qty);
        cart.save(item);
    }

    @Transactional
    public void remove(User user, Long itemId) {
        cart.delete(owned(user, itemId));
    }

    private CartItem owned(User user, Long itemId) {
        CartItem item = cart.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));
        if (!item.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Not your cart item");
        }
        return item;
    }
}
