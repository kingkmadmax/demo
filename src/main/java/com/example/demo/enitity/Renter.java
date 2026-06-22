package com.example.demo.enitity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "renters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Renter {

    @Id
    private String keycloakId;

    @Column(nullable = false, unique = true)
    private String email;

    private String fullName;
    private String role;

    @Column(length = 2048)
    private String lastToken;

    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RentalProduct> products = new ArrayList<>();
}