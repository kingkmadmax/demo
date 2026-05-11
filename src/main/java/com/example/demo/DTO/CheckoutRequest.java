package com.example.demo.DTO;

import lombok.Data;

@Data
public class CheckoutRequest {
    private String location;
    private String receiveDate;
    private String returnDate;
}
//