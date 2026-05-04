package com.example.demo.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakConfig.class);

    @Value("${keycloak.server-url:http://localhost:8080}")
    private String serverUrl;

    @Value("${keycloak.admin.realm:master}")
    private String adminRealm;

    @Value("${keycloak.admin.client-id:admin-backend-client}")
    private String clientId;

    @Value("${keycloak.admin.username:admin}")
    private String username;

    @Value("${keycloak.admin.password:12345}")
    private String password;

    @Bean
    public Keycloak keycloak() {
        logger.info("🔑 Initializing Keycloak Admin Client...");

        Keycloak keycloakInstance = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(adminRealm)
                .clientId(clientId)
                .username(username)
                .password(password)
                .grantType("password")          // ← Using String instead of OAuth2Constants
                .build();

        try {
            // Test the connection
            keycloakInstance.tokenManager().getAccessToken();
            logger.info("✅ Keycloak Admin Client connected successfully!");
        } catch (Exception e) {
            logger.error("❌ Keycloak connection failed. Check if Keycloak is running and credentials are correct.", e);
        }

        return keycloakInstance;
    }
}