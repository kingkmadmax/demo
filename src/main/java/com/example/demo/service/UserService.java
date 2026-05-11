package com.example.demo.service;

import com.example.demo.enitity.UserEnitity;
import com.example.demo.repository.UserRepository;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional // Re-enabled for database safety
@RequiredArgsConstructor // Replaces the manual constructor
@Slf4j // Added for consistency with your controller
public class UserService {

    private final UserRepository userRepository;



    public UserEnitity validateAndSaveUser(Jwt jwt) {
        if (jwt == null) {
            log.error("Sync failed: JWT is null");
            throw new IllegalArgumentException("JWT token cannot be null");
        }

        // Extract information from Keycloak JWT token
        String userId      = jwt.getClaimAsString("sub");
        String username    = jwt.getClaimAsString("preferred_username");
        String email       = jwt.getClaimAsString("email");
        String firstName   = jwt.getClaimAsString("given_name");
        String lastName    = jwt.getClaimAsString("family_name");

        log.debug("Processing sync for user: {} ({})", username, email);

        // Find existing user or create new one
        UserEnitity user = userRepository.findById(userId)
                .orElseGet(() -> {
                    log.info("First time login detected for user ID: {}. Creating new profile.", userId);
                    return new UserEnitity();
                });

        // Update user information
        user.setId(userId);
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setToken(jwt.getTokenValue());

        // Set timestamps
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        user.setUpdatedAt(LocalDateTime.now());

        log.info("Saving/Updating user info in PostgreSQL for: {}", email);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<UserEnitity> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<UserEnitity> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void deleteUser(String id) {
        log.warn("Deleting user record with ID: {}", id);
        userRepository.deleteById(id);
    }
}