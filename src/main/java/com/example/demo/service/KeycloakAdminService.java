package com.example.demo.service;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

@Service
public class KeycloakAdminService {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakAdminService.class);

    private final Keycloak keycloak;
    private final String targetRealm;

    public KeycloakAdminService(Keycloak keycloak,
                                @Value("${keycloak.target.realm:Rent-wepsite}") String targetRealm) {
        this.keycloak = keycloak;
        this.targetRealm = targetRealm;
    }

    public void createRenterUser(String email, String fullName, String phone) {
        UserRepresentation user = prepareUserRepresentation(email, fullName, phone);

        try (Response response = keycloak.realm(targetRealm).users().create(user)) {
            if (response.getStatus() == 201) {
                logger.info("✅ User created successfully in realm {}: {}", targetRealm, email);
            } else if (response.getStatus() == 409) {
                logger.warn("⚠️ User already exists: {}", email);
                throw new RuntimeException("User with this email already exists.");
            } else {
                String error = response.readEntity(String.class);
                logger.error("❌ Keycloak error {}: {}", response.getStatus(), error);
                throw new RuntimeException("Failed to create user. Status: " + response.getStatus());
            }
        } catch (Exception e) {
            logger.error("Critical failure connecting to Keycloak", e);
            throw new RuntimeException("Identity service unavailable: " + e.getMessage());
        }
    }

    private UserRepresentation prepareUserRepresentation(String email, String fullName, String phone) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(email);
        user.setEmail(email);
        user.setFirstName(fullName);
        user.setEmailVerified(true);
        user.setRequiredActions(Collections.emptyList());

        // Set phone as password (Consider improving this in production)
        CredentialRepresentation password = new CredentialRepresentation();
        password.setType(CredentialRepresentation.PASSWORD);
        password.setValue(phone);
        password.setTemporary(false);

        user.setCredentials(List.of(password));
        user.singleAttribute("phoneNumber", phone);

        return user;
    }
}