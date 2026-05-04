package com.example.demo.service;

import com.example.demo.enitity.AgentEntity;
import com.example.demo.repository.AgentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AgentSyncService {
    private final AgentRepository agentRepository;

    @Transactional
    public AgentEntity syncAgent(Jwt jwt) {
        // 1. Get the 'sub' (Subject) from the JWT. This is the Keycloak UUID.
        String subjectId = jwt.getSubject();

        if (subjectId == null) {
            throw new IllegalArgumentException("JWT 'sub' claim is null. Check Keycloak token settings.");
        }

        // 2. Look for the agent. If not found, create a new one and SET THE ID IMMEDIATELY.
        AgentEntity agent = agentRepository.findById(subjectId).orElseGet(() -> {
            AgentEntity newAgent = new AgentEntity();
            newAgent.setId(subjectId); // This ensures the new entity has the Keycloak ID
            return newAgent;
        });

        // 3. Update the fields
        agent.setUsername(jwt.getClaimAsString("preferred_username"));
        agent.setEmail(jwt.getClaimAsString("email"));
        agent.setFirstName(jwt.getClaimAsString("given_name"));
        agent.setLastName(jwt.getClaimAsString("family_name"));
        agent.setLastLogin(LocalDateTime.now());

        // 4. Save and return
        return agentRepository.save(agent);
    }
}