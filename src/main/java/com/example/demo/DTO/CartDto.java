package com.example.demo.DTO;

import lombok.Data;

@Data
public class CartDto {

    private Long productId;
    private String name;
    private double price;
    private int quantity;
    private String image;
}