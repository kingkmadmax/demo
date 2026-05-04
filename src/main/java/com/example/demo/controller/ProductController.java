package com.example.demo.controller;

import com.example.demo.enitity.Rental;
import com.example.demo.service.RentalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "http://localhost:3002"})
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final RentalService rentalService;

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(
            @RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam("price") String price,
            @RequestParam("location") String location,
            @RequestParam("condition") String condition,
            @RequestParam(value = "deposit", required = false) String deposit,
            @RequestParam("description") String description,
            @RequestParam(value = "images", required = false) MultipartFile[] images) {

        log.info("Received request to add product");
        log.info("Request data: name={}, category={}, price={}, location={}, condition={}, deposit={}",
                name, category, price, location, condition, deposit);

        if (images != null) {
            log.info("Number of images received: {}", images.length);
            for (MultipartFile file : images) {
                log.info("Image file: {}", file.getOriginalFilename());
            }
        } else {
            log.warn("No images received in request");
        }

        try {
            log.info("Calling RentalService to create rental...");

            Rental saved = rentalService.createRental(
                    name, category, price, location, condition, deposit, description, images
            );

            log.info("Rental created successfully with ID: {}", saved.getId());

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Listing published successfully!",
                    "id", saved.getId()
            ));

        } catch (Exception e) {
            log.error("Error while creating rental", e);
            e.printStackTrace();

            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }
}