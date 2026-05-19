package com.example.demo.enitity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rentals_product")
@Data
public class RentalProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ownerId;

    private String name;
    private Double price;
    private String category;
    private String location;
    private String condition;
    private Double deposit;

    @Column(length = 2000)
    private String description;

    private String imageUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


    @Column(name = "trending_score", columnDefinition = "DOUBLE PRECISION DEFAULT 0.0")
    private Double trendingScore = 0.0;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @BatchSize(size = 15)
    private List<Review> reviews = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ProductSituation Situation = ProductSituation.AVAILABLE;
    // Helper method to calculate average rating
    public Double getAverageRating() {
        if (reviews.isEmpty()) return 0.0;
        return reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
    }
}