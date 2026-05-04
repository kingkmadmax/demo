package com.example.demo.service;

import com.cloudinary.Cloudinary;
import com.example.demo.enitity.Rental;
import com.example.demo.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentalService {

    private final RentalRepository rentalRepository;
    private final Cloudinary cloudinary;

    public Rental createRental(String name, String category, String price,
                               String location, String condition, String deposit,
                               String description, MultipartFile[] images) throws Exception {

        log.info("Starting rental creation process...");
        log.info("Received data: name={}, category={}, price={}, location={}, condition={}, deposit={}",
                name, category, price, location, condition, deposit);

        List<String> imageUrls = new ArrayList<>();

        if (images != null) {
            log.info("Processing {} images...", images.length);

            for (MultipartFile file : images) {
                if (!file.isEmpty()) {
                    log.info("Uploading image: {}", file.getOriginalFilename());

                    try {
                        Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(),
                                Map.of("folder", "ethirent/products"));

                        String url = (String) result.get("secure_url");
                        imageUrls.add(url);

                        log.info("Image uploaded successfully: {}", url);

                    } catch (Exception e) {
                        log.error("Error uploading image: {}", file.getOriginalFilename(), e);
                        throw e;
                    }
                } else {
                    log.warn("Skipped empty file.");
                }
            }
        } else {
            log.warn("No images provided.");
        }

        log.info("Creating Rental object...");

        Rental rental = new Rental();
        rental.setName(name);
        rental.setCategory(category);
        rental.setPrice(new java.math.BigDecimal(price));
        rental.setLocation(location);
        rental.setCondition(condition);
        rental.setDeposit(deposit != null ? new java.math.BigDecimal(deposit) : null);
        rental.setDescription(description);
        rental.setImages(imageUrls.toArray(new String[0]));
        rental.setStatus("available");

        log.info("Saving rental to database...");

        Rental savedRental = rentalRepository.save(rental);

        log.info("Rental saved successfully with ID: {}", savedRental.getId());

        return savedRental;
    }
}