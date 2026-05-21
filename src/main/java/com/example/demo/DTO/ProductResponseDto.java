package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {

    private Long id;
    private String ownerId;
    private String name;
    private Double price;
    private String category;
    private String location;
    private String condition;
    private Double deposit;
    private String description;
    private String imageUrl;
    private String Situation;
    private String status;
    private double averageRating;


}