package com.example.demo.repository;

import com.example.demo.enitity.RentalProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<RentalProduct, Long> {

    List<RentalProduct> findByOwnerId(String ownerId);


    // For the Marketplace: Filter products by category
    List<RentalProduct> findByCategory(String category);

    // For the Search Bar: Find items by name (case-insensitive)
    List<RentalProduct> findByNameContainingIgnoreCase(String name);

    // For Location-based filtering (important for Addis Ababa markets)
    List<RentalProduct> findByLocation(String location);

    List<RentalProduct> findByOwnerIdAndCategory(String ownerId, String category);
}