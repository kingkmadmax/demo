package com.example.demo.controller;

import com.example.demo.DTO.LoginRequest;
import com.example.demo.DTO.RefreshRequest;
import com.example.demo.DTO.UserAuthControllerDTO;
import com.example.demo.Exception.InvalidCredentialsException;
import com.example.demo.Exception.UserAlreadyExistsException;
import com.example.demo.service.UserAuthService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;




@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class UserAuthController {

    private final UserAuthService userAuthService;
    private final UserService userService;

    // Register endpoint
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserAuthControllerDTO request) {
        try {
            userAuthService.createUser(request);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "success", true,
                            "message", "User created successfully"
                    ));

        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "success", false,
                            "error", e.getMessage()
                    ));

        } catch (Exception e) {
            log.error("Registration failed", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "error", "Registration failed"
                    ));
        }
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Map<String, Object> tokens =
                    userService.login(
                            request.getUsername(),
                            request.getPassword()
                    );

            return ResponseEntity.ok(tokens);

        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "success", false,
                            "error", e.getMessage()
                    ));

        } catch (Exception e) {
            log.error("Login failed", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "error", "Login failed"
                    ));
        }
    }

    // Refresh token endpoint
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {
        try {
            Map<String, Object> tokens =
                    userService.refreshToken(request.getRefreshToken());

            return ResponseEntity.ok(tokens);

        } catch (Exception e) {
            log.error("Token refresh failed", e);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "success", false,
                            "error", "Failed to refresh token"
                    ));
        }
    }
}