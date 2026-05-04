package com.example.demo.enitity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "agents")
@Getter
@Setter
@NoArgsConstructor
public class AgentEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id; // This is the Keycloak 'sub' (Subject)

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    // Constructor for easy manual creation
    public AgentEntity(String id, String username, String email, String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastLogin = LocalDateTime.now();
    }
}