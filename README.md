# 🛒 ShopEase — Full-Stack E-Commerce Application (Spring Boot)

A complete e-commerce web application built with **Spring Boot 3**, **Spring Security**, **Spring Data JPA**, **Thymeleaf** and **H2** (switchable to MySQL).

## Features
- User registration & login (BCrypt-hashed passwords, role-based access: USER / ADMIN)
- Product catalog with search and category filter, product detail page
- Shopping cart (add, update quantity, remove) with stock validation
- Checkout (Cash on Delivery), order history and order detail pages
- Admin panel: add / edit / delete products, view all orders, update order status
- Automatic seed data (12 products, 2 demo accounts) on first run

## Tech Stack
Java 17 · Spring Boot 3.3 · Spring MVC · Spring Security · Spring Data JPA (Hibernate) · Thymeleaf · H2 / MySQL · Maven · Lombok

## Run Locally
Requirements: JDK 17+ and Maven (or use an IDE such as IntelliJ / VS Code / Eclipse).

```bash
mvn spring-boot:run
```
Open http://localhost:8080

| Role  | Email           | Password |
|-------|-----------------|----------|
| Admin | admin@shop.com  | admin123 |
| User  | user@shop.com   | user123  |

## Project Structure
```
src/main/java/com/ecommerce
├── config/       SecurityConfig, DataSeeder
├── controller/   Product, Auth, Cart, Order, Admin controllers
├── model/        User, Product, CartItem, CustomerOrder, OrderItem
├── repository/   Spring Data JPA repositories
└── service/      UserService, CartService, OrderService
src/main/resources
├── templates/    Thymeleaf pages (+ admin/)
└── static/css/   style.css
```

## Using MySQL
Add `mysql-connector-j` to `pom.xml` and update the datasource lines in `application.properties` (example is commented in the file).
