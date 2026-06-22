package com.example.demo.controller;

import com.example.demo.enitity.RenterApplicationEnitiy;
import com.example.demo.repository.OnboardingApplicationRepository;
import com.example.demo.service.KeycloakAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/onboarding/")
@RequiredArgsConstructor
@Slf4j

public class OnboardingAdminController {

    private final OnboardingApplicationRepository repository;
    private final KeycloakAdminService keycloakAdminService;

    // ✅ Get all pending applications for your dashboard
    @GetMapping("/applications")
    public List<RenterApplicationEnitiy> getPendingApplications() {
        return repository.findByStatus("PENDING");
    }

    // ✅ Approve renter with Transactional safety
    @PostMapping("/approve/{id}")
    @Transactional
    public ResponseEntity<?> approveRenter(@PathVariable Long id) {
        log.info("Approving application ID: {}", id);

        RenterApplicationEnitiy application = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        try {
            // 1. Create account in Keycloak using phone as password
            keycloakAdminService.createRenterUser(
                    application.getEmail(),
                    application.getFullName(),
                    application.getPhone()
            );

            // 2. Clear from local database only if Keycloak succeeds
            repository.delete(application);

            return ResponseEntity.ok(Map.of("message", "Renter account created in Keycloak successfully"));
        } catch (Exception e) {
            log.error("Keycloak Provisioning Failed: {}", e.getMessage());
            // Return 500 error so your Next.js frontend knows it failed
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Reject application
    @DeleteMapping("/reject/{id}")
    public ResponseEntity<?> reject(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Application rejected and removed"));
        }
        return ResponseEntity.notFound().build();
    }
}