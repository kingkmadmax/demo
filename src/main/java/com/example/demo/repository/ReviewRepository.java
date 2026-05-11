package com.example.demo.repository;

import com.example.demo.enitity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // This allows you to find all reviews for a specific product
    List<Review> findByProductId(Long productId);
}