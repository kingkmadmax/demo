package com.example.demo.service;

import com.example.demo.DTO.UserAuthControllerDTO;
import com.example.demo.Exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class UserAuthService {
    @Value("${keycloak.admin.server-url}")
    private String keycloakUrl;

    @Value("${keycloak.target.realm}")
    private String realm;

    @Value("${keycloak.admin.client-id}")
    private String clientId;

    @Value("${keycloak.admin.client-secret}")
    private String clientSecret;

    private String getAdminToken() throws Exception {
        RestTemplate rest = new RestTemplate();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        ResponseEntity<Map> response = rest.exchange(
                keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token",
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                Map.class
        );

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Failed to get admin token");
        }

        return (String) response.getBody().get("access_token");
    }

    public void createUser(UserAuthControllerDTO request) throws Exception {
        String adminToken = getAdminToken();
        RestTemplate rest = new RestTemplate();

        Map<String, Object> userPayload = Map.of(
                "username", request.getUsername(),
                "email", request.getEmail(),
                "enabled", true,
                "emailVerified", true,
                "firstName", request.getFirstName(),
                "lastName", request.getLastName(),
                "credentials", List.of(Map.of(
                        "type", "password",
                        "value", request.getPassword(),
                        "temporary", false
                ))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);

        ResponseEntity<String> response = rest.exchange(
                keycloakUrl + "/admin/realms/" + realm + "/users",
                HttpMethod.POST,
                new HttpEntity<>(userPayload, headers),
                String.class
        );

        if (response.getStatusCode() == HttpStatus.CONFLICT) {
            throw new UserAlreadyExistsException("Username or email already exists");
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Keycloak user creation failed: " + response.getBody());
        }
    }
}
