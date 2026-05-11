package com.example.demo.enitity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "rental_orders")
@Data 
public class Order {

    @Id // Use ONLY jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Move this here!
    private Long id;

    @Column(nullable = false)
    private String userId; // This is the Keycloak ID, handled manually

    @Column(nullable = false)
    private Double totalAmount;

    @Column(nullable = false)
    private String status; 

    @Column(unique = true, nullable = false)
    private String transactionReference;

    private LocalDateTime createdAt;
    private LocalDateTime paidAt;

    private Long productId;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "PENDING";
        }
    }
}