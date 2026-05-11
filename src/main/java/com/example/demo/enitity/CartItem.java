package com.example.demo.enitity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cart_items")
@Data
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private Long productId;
    private String name;
    private double price;
    private String image;
    private int quantity;

    private Double deposit;        // ← Add this field

    // Optional: You can add a constructor for convenience
    public CartItem(String userId, Long productId, String name, double price,
                    String image, int quantity, Double deposit) {
        this.userId = userId;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
        this.deposit = deposit;
    }

    public CartItem() {} // Default constructor needed by JPA
}