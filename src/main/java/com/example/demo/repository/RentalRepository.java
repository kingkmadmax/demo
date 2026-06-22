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

    // Fix 1: Changed from findByOwnerId to match Keycloak property mapping
    @EntityGraph(attributePaths = {"owner"})
    List<RentalProduct> findByOwnerKeycloakId(String keycloakId);

    // This one was already perfectly structured!
    @EntityGraph(attributePaths = {"owner"})
    List<RentalProduct> findByOwnerKeycloakIdAndCategory(String keycloakId, String category);


    // ---- Paging Queries ----

    @EntityGraph(attributePaths = {"reviews"})
    Page<RentalProduct> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"reviews"})
    Page<RentalProduct> findByCategory(String category, Pageable pageable);

    @EntityGraph(attributePaths = {"reviews"})
    Page<RentalProduct> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @EntityGraph(attributePaths = {"reviews"})
    Page<RentalProduct> findByLocation(String location, Pageable pageable);
}