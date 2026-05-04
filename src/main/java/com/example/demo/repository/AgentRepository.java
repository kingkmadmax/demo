package com.example.demo.repository;

import com.example.demo.enitity.AgentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<AgentEntity, String> {

    // Extra helper methods for your business logic
    Optional<AgentEntity> findByEmail(String email);

    Optional<AgentEntity> findByUsername(String username);
}