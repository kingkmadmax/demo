package com.example.demo.controller;

import com.example.demo.enitity.UserEnitity;
import com.example.demo.service.UserServic;
import com.example.demo.util.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserServic userService;

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateAndSaveUser(
            @AuthenticationPrincipal Jwt jwt, 
            @RequestHeader Map<String, String> headers) {

        log.info("=== DEBUG: New Request to /validate-token ===");

        // 1. Check if JWT is null first (Standard Spring Security check)
        if (jwt == null) {
            log.warn("RESULT: JWT object is NULL.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                        "success", false,
                        "message", "No valid JWT token found."
                    ));
        }

        // 2. Verify Keycloak ID from our Utility
        String keycloakId = SecurityUtil.getCurrentKeycloakId();
        if (keycloakId == null) {
            log.error("Failed to extract Keycloak ID from SecurityContext");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token Principal");
        }

        try {
            // 3. Process the User Sync
            log.info("Processing sync for Keycloak ID: {}", keycloakId);
            UserEnitity savedUser = userService.validateAndSaveUser(jwt);

            log.info("DATABASE SUCCESS: Saved User ID: {}", savedUser.getId());

            // 4. Return Lean Session Data (Only what Next.js needs)
            return ResponseEntity.ok(Map.of(
                "success", true,
                "userId", savedUser.getId(),
                "username", savedUser.getUsername()
            ));

        } catch (Exception e) {
            log.error("DATABASE ERROR: Failed to save user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
        // Removed the extra return statement that was causing the error
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        log.info("Test endpoint hit successfully");
        return ResponseEntity.ok(Map.of(
            "message", "UserController is working correctly!",
            "status", "OK",
            "timestamp", System.currentTimeMillis()
        ));
    }
}