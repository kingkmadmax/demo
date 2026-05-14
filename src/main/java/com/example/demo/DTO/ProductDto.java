package com.example.demo.DTO;

import lombok.Data;

@Data
public class  ProductDto {

    private String imageUrl;
    private String name;
    private String category;
    private Double price;
    private Double deposit;
    private String condition;
    private String location;
    private String description;
    private String userId;
}