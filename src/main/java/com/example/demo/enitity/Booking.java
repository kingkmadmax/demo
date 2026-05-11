package com.example.demo.enitity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Identification ---
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String fid; // Found in your screenshot

    // --- Images ---
    private String faceImage;
    private String idImage;
    private String image;
    private LocalDate receiveDate;
    private LocalDate returnDate;
    // Product thumbnail

    // --- Product Details ---
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double price;
    private Double deposit;
    private Double totalPrice;

    // --- Dates & Logistics ---
    private String location;
    private String rentalStartDate; // Stored as varchar(255) per screenshot

   // Stored as date
    private LocalDateTime bookingDate; // Stored as timestamp

    private String status;
}