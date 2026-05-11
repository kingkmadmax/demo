package com.example.demo.DTO;



import lombok.Data;

@Data
public class CartRequest {
//
   private Long productId;
   private String productName;
   private Double price;
   private int quantity;
}