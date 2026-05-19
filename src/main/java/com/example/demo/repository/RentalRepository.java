package com.example.demo.repository;

import com.example.demo.enitity.RentalProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<RentalProduct, Long> {

    List<RentalProduct> findByOwnerId(String ownerId);

    List<RentalProduct> findByOwnerIdAndCategory(String ownerId, String category);

    @EntityGraph(attributePaths = {"reviews"})
    Page<RentalProduct> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"reviews"})
    Page<RentalProduct> findByCategory(String category, Pageable pageable);

    @EntityGraph(attributePaths = {"reviews"})
    Page<RentalProduct> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @EntityGraph(attributePaths = {"reviews"})
    Page<RentalProduct> findByLocation(String location, Pageable pageable);
}
