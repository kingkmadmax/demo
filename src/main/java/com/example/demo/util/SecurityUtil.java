package com.example.demo.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class SecurityUtil {

    // This must be a static method in a class
    public static String getCurrentKeycloakId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (principal instanceof Jwt) {
            return ((Jwt) principal).getSubject(); // This is the Keycloak 'sub' UUID
        }
        return null;
    }
}