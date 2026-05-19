package com.example.demo.enitity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String author;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private Integer rating;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // The proper way: Link back to the parent Product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference
    private RentalProduct product;
}