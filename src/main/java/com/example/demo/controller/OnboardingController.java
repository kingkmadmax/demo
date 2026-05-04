package com.example.demo.controller;

import com.example.demo.enitity.AgentEntity;
import com.example.demo.service.AgentSyncService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.example.demo.enitity.RenterApplicationEnitiy;
import com.example.demo.service.RenterApplicationService;

@RestController
@RequestMapping("/api/onboarding")

public class OnboardingController {

    private static final Logger logger = LoggerFactory.getLogger(OnboardingController.class);

    @Autowired
    private RenterApplicationService service;

    @PostMapping("/apply")
    public ResponseEntity<String> apply(@RequestBody RenterApplicationEnitiy application) {
        try {
            service.saveApplication(application);

            logger.info("Application saved successfully for email: {}", application.getEmail());

            return ResponseEntity.ok("Application submitted successfully!");

        } catch (Exception e) {

            logger.error("Error while saving application for email: {}", application.getEmail());
            logger.error("Root cause: ", e);

            return ResponseEntity.status(500)
                    .body("Error: " + e.getMessage());
        }
    }

    @RestController
    @RequestMapping("/api/auth")
    @RequiredArgsConstructor
    public static class AgentAuthController {
        private final AgentSyncService syncService;

        @PostMapping("/agent-sync")
        public ResponseEntity<?> validateAndStore(@AuthenticationPrincipal Jwt jwt) {
            // If the code reaches here, Spring Security has already validated the JWT!
            AgentEntity agent = syncService.syncAgent(jwt);
            return ResponseEntity.ok(agent);
        }
    }
}