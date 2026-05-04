package com.example.demo.controller;

import com.example.demo.enitity.UserEnitity;
import com.example.demo.service.AgentSyncService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AgentAuthController {
    private final AgentSyncService syncService;

    @PostMapping("/agent-syn")
    public ResponseEntity<?> sync(@AuthenticationPrincipal Jwt jwt) {
        // Validation happens automatically via SecurityConfig
        return ResponseEntity.ok(syncService.syncAgent(jwt));
    }
}