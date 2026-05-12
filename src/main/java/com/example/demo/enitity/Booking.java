package com.example.demo.enitity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
@Data
public class Booking {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "product_id", nullable = false)
        private RentalProduct product;

        private String userId; // Keycloak UUID String
        private String customerName;
        private String email;
        private String phone;
        private String pickupLocation;
        private String fidaId; // National ID Proxy

        @Column(columnDefinition = "TEXT")
        private String faceImageUrl;

        @Column(columnDefinition = "TEXT")
        private String idImageUrl;

        private LocalDateTime receiveDate;
        private LocalDateTime returnDate;
        private Integer rentalDays;
        private String status = "PENDING";

        private LocalDateTime createdAt;

        @PrePersist
        protected void onCreate() {
                this.createdAt = LocalDateTime.now();
        }
}