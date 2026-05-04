package com.example.demo.controller;

import com.example.demo.enitity.RenterApplicationEnitiy;
import com.example.demo.repository.OnboardingApplicationRepository;
import com.example.demo.service.KeycloakAdminService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/onboarding")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:3002"})
public class OnboardingAdminController {

    private static final Logger logger = LoggerFactory.getLogger(OnboardingAdminController.class);

    private final OnboardingApplicationRepository repository;
    private final KeycloakAdminService keycloakAdminService;

    @GetMapping("/applications")
    public ResponseEntity<List<RenterApplicationEnitiy>> getPendingApplications() {
        List<RenterApplicationEnitiy> applications = repository.findByStatus("PENDING");
        logger.info("Fetched {} pending applications", applications.size());
        return ResponseEntity.ok(applications);
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<String> approve(@PathVariable Long id) {
        return repository.findById(id)
                .map(app -> {
                    try {
                        logger.info("Approving application ID: {} for email: {}", id, app.getEmail());

                        // Create user in Keycloak
                        keycloakAdminService.createRenterUser(
                                app.getEmail(),
                                app.getFullName(),
                                app.getPhone()
                        );

                        // Delete from pending list after successful Keycloak sync
                        repository.delete(app);

                        logger.info("Successfully approved and synced user: {}", app.getEmail());
                        return ResponseEntity.ok("Renter approved and Keycloak account created successfully.");

                    } catch (Exception e) {
                        logger.error("Failed to approve application ID: {}", id, e);
                        return ResponseEntity.internalServerError()
                                .body("Error syncing to Keycloak: " + e.getMessage());
                    }
                })
                .orElseGet(() -> {
                    logger.warn("Application with ID {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/reject/{id}")
    public ResponseEntity<String> reject(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            logger.info("Application ID {} rejected and deleted", id);
            return ResponseEntity.ok("Application rejected successfully.");
        }

        logger.warn("Application with ID {} not found for rejection", id);
        return ResponseEntity.notFound().build();
    }
}