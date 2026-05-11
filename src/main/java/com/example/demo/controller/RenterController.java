package com.example.demo.controller;

import com.example.demo.enitity.Renter;
import com.example.demo.service.RenterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3002","http://localhost:3001"})
public class RenterController {

    private final RenterService renterService;

    @PostMapping("/login-sync")
    public ResponseEntity<?> loginSync(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) {
            return ResponseEntity.status(401).body(Map.of("message", "No token provided"));
        }

        try {
            Renter renter = renterService.syncRenterOnLogin(jwt);

            log.info("Renter sync successful for: {}", renter.getEmail());

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "keycloakId", renter.getKeycloakId(),
                    "email", renter.getEmail(),
                    "fullName", renter.getFullName(),
                    "role", renter.getRole()
            ));
        } catch (Exception e) {
            log.error("Sync Error", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Failed to sync user data", "error", e.getMessage()));
        }
    }
}