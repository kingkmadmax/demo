package com.example.demo.service;

import com.example.demo.Exception.InvalidCredentialsException;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Value("${keycloak.admin.server-url}")
    private String keycloakUrl;

    @Value("${keycloak.target.realm}")
    private String realm;

    @Value("${keycloak.admin.client-id}")
    private String loginClientId;

    @Value("${keycloak.admin.client-secret}")
    private String loginClientSecret;

    public Map<String, Object> login(String username, String password) {
        RestTemplate rest = new RestTemplate();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", loginClientId);
        body.add("client_secret", loginClientSecret);
        body.add("username", username);
        body.add("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        try {
            ResponseEntity<Map> response = rest.exchange(
                    keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token",
                    HttpMethod.POST,
                    new HttpEntity<>(body, headers),
                    Map.class
            );

            return Map.of(
                    "success", true,
                    "access_token", response.getBody().get("access_token"),
                    "refresh_token", response.getBody().get("refresh_token")
            );
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new InvalidCredentialsException("Invalid username or password");
        } catch (HttpClientErrorException.BadRequest e) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    public Map<String, Object> refreshToken(String refreshToken) {
        RestTemplate rest = new RestTemplate();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", loginClientId);
        body.add("client_secret", loginClientSecret);
        body.add("refresh_token", refreshToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        ResponseEntity<Map> response = rest.exchange(
                keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token",
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                Map.class
        );

        return Map.of(
                "success", true,
                "access_token", response.getBody().get("access_token"),
                "refresh_token", response.getBody().get("refresh_token")
        );
    }

    // ... your existing validateAndSaveUser, getUserById, etc. stay unchanged
}