package com.ecommerce.config;

import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/** Seeds demo accounts and products on first run. */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository users;
    private final ProductRepository products;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (users.count() == 0) {
            users.save(make("Admin", "admin@shop.com", "admin123", "ROLE_ADMIN"));
            users.save(make("Demo User", "user@shop.com", "user123", "ROLE_USER"));
        }
        if (products.count() == 0) {
            products.saveAll(List.of(
                new Product("Wireless Headphones", "Over-ear Bluetooth headphones with 30-hour battery life and deep bass.", "2499.00", 25, "Electronics", img("headphones")),
                new Product("Smart Watch", "Fitness tracker with heart-rate monitor, GPS and 7-day battery.", "3999.00", 15, "Electronics", img("smartwatch")),
                new Product("Bluetooth Speaker", "Portable waterproof speaker with 360 degree sound.", "1799.00", 30, "Electronics", img("speaker")),
                new Product("Mechanical Keyboard", "RGB backlit mechanical keyboard with blue switches.", "2999.00", 20, "Electronics", img("keyboard")),
                new Product("Men's Casual Shirt", "Breathable cotton shirt, regular fit, available in multiple colours.", "799.00", 50, "Fashion", img("shirt")),
                new Product("Running Shoes", "Lightweight cushioned running shoes for everyday training.", "1999.00", 40, "Fashion", img("shoes")),
                new Product("Denim Jacket", "Classic blue denim jacket with a comfortable fit.", "1499.00", 18, "Fashion", img("jacket")),
                new Product("Java: The Complete Reference", "Comprehensive guide to Java programming for students and professionals.", "699.00", 60, "Books", img("javabook")),
                new Product("Clean Code", "A handbook of agile software craftsmanship by Robert C. Martin.", "549.00", 45, "Books", img("cleancode")),
                new Product("Data Structures Made Easy", "Problem-solving guide to data structures and algorithms.", "449.00", 35, "Books", img("dsbook")),
                new Product("Ceramic Coffee Mug Set", "Set of 4 elegant ceramic mugs, microwave and dishwasher safe.", "599.00", 55, "Home", img("mugs")),
                new Product("LED Desk Lamp", "Adjustable desk lamp with 3 colour modes and USB charging port.", "899.00", 28, "Home", img("lamp"))
            ));
        }
    }

    private User make(String name, String email, String password, String role) {
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setPassword(encoder.encode(password));
        u.setRole(role);
        return u;
    }

    private String img(String seed) {
        return "https://picsum.photos/seed/" + seed + "/400/300";
    }
}
