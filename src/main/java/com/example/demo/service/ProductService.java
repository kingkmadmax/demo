package com.example.demo.service;

import com.example.demo.DTO.ProductResponseDto;
import com.example.demo.enitity.RentalProduct;
import com.example.demo.enitity.Renter;
import com.example.demo.enitity.Review;
import com.example.demo.repository.RentalRepository;
import com.example.demo.repository.RenterRepository; // 1. Added import
import com.example.demo.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final RentalRepository rentalRepository;
    private final ReviewRepository reviewRepository;
    private final RenterRepository renterRepository; // 2. Added repository injection


    public List<RentalProduct> getMyListings(String userId) {
        log.info("Fetching listings for user: {}", userId);
        // 3. Changed from findByOwnerId to findByOwnerKeycloakId
        return rentalRepository.findByOwnerKeycloakId(userId);
    }

    public Page<RentalProduct> getAllProducts(Pageable pageable) {
        log.info("Fetching all marketplace products with pagination");
        return rentalRepository.findAll(pageable);
    }

    public Optional<RentalProduct> getProductById(Long id) {
        log.info("Fetching product with ID: {}", id);
        return rentalRepository.findById(id);
    }

    // ==================== NEW METHOD: Fetch Single Product DTO for Frontend ====================
    public ProductResponseDto getProductDetails(Long id) {
        RentalProduct product = rentalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return convertToDto(product);
    }

    public Page<RentalProduct> getProductsByCategory(String category, Pageable pageable) {
        log.info("Fetching marketplace products for category: {} with pagination", category);
        return rentalRepository.findByCategory(category, pageable);
    }

    // ==================== UPDATED METHOD (Sends Owner Details) ====================
    public Page<ProductResponseDto> getAllProductsWithRating(Pageable pageable, String category) {
        Page<RentalProduct> productPage;

        if (category != null && !category.trim().isEmpty()) {
            productPage = rentalRepository.findByCategory(category, pageable);
        } else {
            productPage = rentalRepository.findAll(pageable);
        }

        log.info("Fetching products with average rating - Page: {}, Category: {}",
                pageable.getPageNumber(), category);

        // 4. Cleaned up to use our reusable converter method
        return productPage.map(this::convertToDto);
    }

    // ==================== REUSABLE CONVERTER (Maps Entity to DTO) ====================
    private ProductResponseDto convertToDto(RentalProduct product) {
        ProductResponseDto dto = new ProductResponseDto();

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory());
        dto.setLocation(product.getLocation());
        dto.setCondition(product.getCondition());
        dto.setDeposit(product.getDeposit());
        dto.setDescription(product.getDescription());
        dto.setImageUrl(product.getImageUrl());
        dto.setSituation(product.getSituation() != null ? product.getSituation().name() : null);

        // Uses the entity's built-in calculation method
        double avgRating = product.getAverageRating();
        dto.setAverageRating(Math.round(avgRating * 100.0) / 100.0);

        // 5. EXTRACT AND SEND OWNER DATA TO FRONTEND RIGHT HERE
        Renter owner = product.getOwner();
        if (owner != null) {
            dto.setOwnerId(owner.getKeycloakId());
            dto.setOwnerName(owner.getFullName());  // <-- Sends the name to the frontend!

        }

        return dto;
    }
    // =================================================================

    @Transactional
    public Review addReviewToProduct(Long productId, Review review) {
        RentalProduct product = rentalRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        review.setProduct(product);
        product.getReviews().add(review);

        return reviewRepository.save(review);
    }

    // ==================== UPDATED CREATION METHOD ====================
    public RentalProduct createProduct(
            String imageUrl, String name, String category, Double price,
            Double deposit, String condition, String location, String description,
            String userId
    ) {
        if (imageUrl == null || imageUrl.isEmpty()) throw new IllegalArgumentException("Image URL is required");
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Product name is required");
        if (userId == null || userId.isEmpty()) throw new IllegalArgumentException("Owner ID is required");

        // 6. Look up the real Renter profile from the database before storing the relation link
        Renter owner = renterRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Renter profile not found for Keycloak ID: " + userId));

        RentalProduct product = new RentalProduct();


        product.setOwner(owner);

        product.setImageUrl(imageUrl);
        product.setName(name);
        product.setCategory(category);
        product.setPrice(price != null ? price : 0.0);
        product.setDeposit(deposit != null ? deposit : 0.0);
        product.setCondition(condition);
        product.setLocation(location);
        product.setDescription(description);

        log.info("Saving product linked directly to owner database object");
        return rentalRepository.save(product);
    }
}