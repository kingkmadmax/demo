package com.example.demo.service;

import com.example.demo.enitity.ProductSituation;
import com.example.demo.enitity.RentalProduct;
import com.example.demo.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final RentalRepository rentalRepository;

    public List<RentalProduct> getMyListings(String userId) {
        log.info("Fetching listings for user: {}", userId);
        return rentalRepository.findByOwnerId(userId);
    }
    public List<RentalProduct> getAllProducts() {
        log.info("Fetching all marketplace products");
        return rentalRepository.findAll();
    }
    public Optional<RentalProduct> getProductById(Long id) {
        log.info("Fetching product with ID: {}", id);
        return rentalRepository.findById(id);
    }
    public RentalProduct createProduct(
            String imageUrl,
            String name,
            String category,
            Double price,
            Double deposit,
            String condition,
            String location,
            String description,
            String userId
    ) {

        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new IllegalArgumentException("Image URL is required");
        }

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Product name is required");
        }

        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Owner ID is required");
        }


        RentalProduct product = new RentalProduct();
        product.setOwnerId(userId);
        product.setImageUrl(imageUrl);
        product.setName(name);
        product.setCategory(category);
        product.setPrice(price != null ? price : 0.0);
        product.setDeposit(deposit != null ? deposit : 0.0);
        product.setCondition(condition);
        product.setLocation(location);
        product.setDescription(description);

        log.info(" Saving product for user {}", userId);

        return rentalRepository.save(product);
    }
}