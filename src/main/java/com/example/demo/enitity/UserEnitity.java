package com.example.demo.enitity;

import java.time.LocalDateTime;


import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEnitity {

    @Id
    private String id;                    // Keycloak 'sub' (user ID)

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    private String firstName;
    private String lastName;

    @Column(length = 2000)                // Token can be long
    private String token;                 // Optional: raw JWT token

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ====================== Constructors ======================

    public UserEnitity () {
        // Default constructor required by JPA
    }

    public UserEnitity (String id, String username, String email, String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // ====================== Getters & Setters ======================

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Optional: Helper method for full name
    public String getFullName() {
        return (firstName != null ? firstName : "") + " " + 
               (lastName != null ? lastName : "").trim();
    }
}