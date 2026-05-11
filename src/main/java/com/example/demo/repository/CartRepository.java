package com.example.demo.repository;

import com.example.demo.enitity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUserId(String userId);

    // Add this method
    void deleteAllByUserId(String userId);

    // Optional: You can also add this for safety
    boolean existsByUserId(String userId);
}