package com.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter @Setter @NoArgsConstructor
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name is required")
    private String name;
    @Column(length = 1000)
    private String description;
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price cannot be negative")
    private BigDecimal price;
    @Min(value = 0, message = "Stock cannot be negative")
    private int stock;
    private String category;
    private String imageUrl;

    public Product(String name, String description, String price, int stock, String category, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = new BigDecimal(price);
        this.stock = stock;
        this.category = category;
        this.imageUrl = imageUrl;
    }
}
