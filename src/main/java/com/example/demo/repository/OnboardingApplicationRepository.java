package com.example.demo.repository;

import com.example.demo.enitity.RenterApplicationEnitiy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OnboardingApplicationRepository extends JpaRepository<RenterApplicationEnitiy, Long> {
    // Find only the people waiting for approval
    List<RenterApplicationEnitiy> findByStatus(String status);
}