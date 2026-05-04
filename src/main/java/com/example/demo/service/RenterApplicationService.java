package com.example.demo.service;

import com.example.demo.enitity.RenterApplicationEnitiy;
import com.example.demo.repository.RenterApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RenterApplicationService {

    private static final Logger log = LoggerFactory.getLogger(RenterApplicationService.class);

    private final RenterApplicationRepository repository;

    // Constructor injection (recommended instead of @Autowired)
    public RenterApplicationService(RenterApplicationRepository repository) {
        this.repository = repository;
    }


    public RenterApplicationEnitiy saveApplication(RenterApplicationEnitiy application) {

        log.info("📦 Saving renter application for email={}", application.getEmail());

        try {
            // Optional debug log before DB call
            log.debug("➡️ Request payload: fullName={}, phone={}",
                    application.getFullName(),
                    application.getPhone());

            RenterApplicationEnitiy saved = repository.save(application);

            log.info("✅ Successfully saved application with id={} email={}",
                    saved.getId(),
                    saved.getEmail());

            return saved;

        } catch (Exception e) {

            log.error("❌ Failed to save application for email={}",
                    application.getEmail(), e);

            throw e;
        }
    }
}