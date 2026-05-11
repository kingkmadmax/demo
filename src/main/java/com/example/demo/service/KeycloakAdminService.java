package com.example.demo.service;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeycloakAdminService {

    private final Keycloak keycloak;

    @Value("${keycloak.target.realm}")
    private String targetRealm;

    public KeycloakAdminService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public void createRenterUser(String email, String fullName, String phone) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(fullName.replace(" ", "").toLowerCase());
        user.setEmail(email);
        user.setFirstName(fullName);
        user.setEmailVerified(true);

        CredentialRepresentation pass = new CredentialRepresentation();
        pass.setType(CredentialRepresentation.PASSWORD);
        pass.setValue(phone);
        pass.setTemporary(false);
        user.setCredentials(List.of(pass));

        // Use a try-with-resources to ensure the response is closed
        try (Response response = keycloak.realm(targetRealm).users().create(user)) {

            if (response.getStatus() == 201) {
                String userId = CreatedResponseUtil.getCreatedId(response);

                RoleRepresentation renterRole = keycloak.realm(targetRealm)
                        .roles()
                        .get("renter")
                        .toRepresentation();

                keycloak.realm(targetRealm)
                        .users()
                        .get(userId)
                        .roles()
                        .realmLevel()
                        .add(List.of(renterRole));

                System.out.println("✅ User created and assigned 'renter' role: " + fullName);
            } else {
                String errorMsg = response.readEntity(String.class);
                throw new RuntimeException("Keycloak creation failed: " + errorMsg);
            }
        }
    }
}