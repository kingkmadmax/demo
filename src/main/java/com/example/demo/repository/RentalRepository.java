package com.example.demo.repository;

import com.example.demo.enitity.RentalProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<RentalProduct, Long> {

    List<RentalProduct> findByOwnerId(String ownerId);

    List<RentalProduct> findByOwnerIdAndCategory(String ownerId, String category);
}