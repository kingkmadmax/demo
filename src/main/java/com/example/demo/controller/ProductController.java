package com.example.demo.controller;

import com.example.demo.DTO.ProductResponseDto;
import com.example.demo.enitity.RentalProduct;
import com.example.demo.DTO.ProductDto;
import com.example.demo.enitity.Review;
import com.example.demo.repository.RentalRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.service.ProductService;
import com.example.demo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final RentalRepository rentalRepository;
    private final ReviewRepository reviewRepository;
    private final ProductService productService;
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/my-listings")
    public ResponseEntity<List<RentalProduct>> getMyListings(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }

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
    @GetMapping("/{productId}/Rating")
    public ResponseEntity<Map<String, Object>> getAverageRating(@PathVariable Long productId) {
        double averageRating = reviewService.getMeanRatingForProduct(productId);

        Map<String, Object> response = new HashMap<>();
        response.put("productId", productId);
        response.put("averageRating", Math.round(averageRating * 100.0) / 100.0); // Round

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{productId}/reviews")
    public ResponseEntity<Review> addReview(@PathVariable Long productId, @RequestBody Review review) {
        Review savedReview = productService.addReviewToProduct(productId, review);
        return ResponseEntity.ok(savedReview);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(required = false) String category) {

        Pageable pageable = PageRequest.of(page, size);

        Page<ProductResponseDto> responsePage = productService
                .getAllProductsWithRating(pageable, category);

        return ResponseEntity.ok(responsePage);
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