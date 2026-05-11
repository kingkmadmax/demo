package com.example.demo.repository;

import com.example.demo.enitity.Renter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RenterRepository extends JpaRepository<Renter, String> {
    Optional<Renter> findByEmail(String email);
    Optional<Renter> findByKeycloakId(String keycloakId);
}