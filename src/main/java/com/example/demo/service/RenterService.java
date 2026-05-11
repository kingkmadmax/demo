package com.example.demo.service;

import com.example.demo.enitity.Renter;
import com.example.demo.repository.RenterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RenterService {

    private final RenterRepository renterRepository;

    @Transactional
    public Renter syncRenterOnLogin(Jwt jwt) {
        String keycloakId = jwt.getSubject();
        String email = jwt.getClaimAsString("email");
        String firstName = jwt.getClaimAsString("given_name");
        String lastName = jwt.getClaimAsString("family_name");

        log.info("Syncing login for renter: {}", email);

        Renter renter = renterRepository.findByKeycloakId(keycloakId)
                .orElseGet(() -> {
                    Renter newRenter = new Renter();
                    newRenter.setKeycloakId(keycloakId);
                    newRenter.setCreatedAt(LocalDateTime.now());
                    return newRenter;
                });

        // Fixed: Properly build fullName before setting
        String fullName = ((firstName != null ? firstName : "") + " " +
                (lastName != null ? lastName : "")).trim();

        renter.setEmail(email);
        renter.setFullName(fullName);
        renter.setLastLogin(LocalDateTime.now());
        renter.setUpdatedAt(LocalDateTime.now());
        renter.setLastToken(jwt.getTokenValue());


        // Extract role from realm_access.roles
        try {
            var realmAccess = jwt.getClaimAsMap("realm_access");
            if (realmAccess != null && realmAccess.get("roles") instanceof List<?> roles) {
                if (roles.contains("renter")) {
                    renter.setRole("RENTER");
                } else if (roles.contains("admin")) {
                    renter.setRole("ADMIN");
                } else {
                    renter.setRole("RENTER"); // default
                }
            }
        } catch (Exception e) {
            log.warn("Could not extract roles from token for user: {}", email);
            renter.setRole("RENTER"); // default fallback
        }

        return renterRepository.save(renter);
    }
}