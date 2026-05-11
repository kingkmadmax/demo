package com.example.demo.controller;

import com.example.demo.enitity.RentalProduct;
import com.example.demo.DTO.ProductDto;
import com.example.demo.enitity.Review;
import com.example.demo.repository.RentalRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3002","http://localhost:3001"})
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final RentalRepository rentalRepository;
    private final ReviewRepository reviewRepository;
    private final ProductService productService;

    @GetMapping("/my-listings")
    public ResponseEntity<List<RentalProduct>> getMyListings(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }

        // Extract the unique ID from the JWT token automatically
        String userId = authentication.getName();

        List<RentalProduct> myProducts = productService.getMyListings(userId);
        return ResponseEntity.ok(myProducts);


    }
    @GetMapping("/{id}")
    public ResponseEntity<RentalProduct> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, Authentication authentication) {
        String currentUserId = authentication.getName();

        RentalProduct product = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));




        rentalRepository.delete(product);
        return ResponseEntity.ok("Product deleted successfully");
    }
    @PostMapping("/{productId}/reviews")
    public ResponseEntity<Review> addReview(@PathVariable Long productId, @RequestBody Review review) {
        return rentalRepository.findById(productId).map(product -> {
            review.setProduct(product);
            Review savedReview = reviewRepository.save(review);
            return ResponseEntity.ok(savedReview);
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<RentalProduct>> getAllProducts() {
        List<RentalProduct> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping(value = "/add", consumes = "application/json")
    public ResponseEntity<RentalProduct> addProduct(
            @RequestBody ProductDto dto,
            Authentication authentication
    ) {

        if (authentication == null) {
            throw new RuntimeException("Unauthorized request");
        }


        String userId = authentication.getName();

        log.info("🚀 Creating product: {} for user: {}", dto.getName(), userId);

        RentalProduct saved = productService.createProduct(
                dto.getImageUrl(),
                dto.getName(),
                dto.getCategory(),
                dto.getPrice(),
                dto.getDeposit(),
                dto.getCondition(),
                dto.getLocation(),
                dto.getDescription(),
                userId
        );

        return ResponseEntity.ok(saved);
    }
}