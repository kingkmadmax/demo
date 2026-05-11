package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentResponse(
        String status,
        String message,
        PaymentData data
) {
   public record PaymentData(
           @JsonProperty("checkout_url") String checkoutUrl
   ) {}
}