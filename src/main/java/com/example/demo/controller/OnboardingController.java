package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.enitity.RenterApplicationEnitiy;
import com.example.demo.service.RenterApplicationService;

@RestController
@RequestMapping("/api/onboarding")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3002","http://localhost:3001"})
public class OnboardingController {

    private static final Logger logger = LoggerFactory.getLogger(OnboardingController.class);

    @Autowired
    private RenterApplicationService service;

    @PostMapping("/apply")
    public ResponseEntity<String> apply(@RequestBody RenterApplicationEnitiy application) {

        // 🔍 LOG INPUT RECEIVED
        

        try {
            service.saveApplication(application);

            logger.info("Application saved successfully for email: {}", application.getEmail());

            return ResponseEntity.ok("Application submitted successfully!");

        } catch (Exception e) {

            // 🔥 FULL ERROR LOG
            logger.error("Error while saving application for email: {}", application.getEmail());
            logger.error("Root cause: ", e);

            return ResponseEntity.status(500)
                    .body("Error: " + e.getMessage());
        }
    }
}