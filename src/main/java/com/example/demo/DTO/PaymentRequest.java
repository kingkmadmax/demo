package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record PaymentRequest(

        @JsonProperty("amount")
        @NotBlank(message = "Amount is required")
        String amount,

        @JsonProperty("currency")
        @NotBlank(message = "Currency is required")
        String currency,

        @JsonProperty("email")
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @JsonProperty("first_name")
        @NotBlank(message = "First name is required")
        String firstName,

        @JsonProperty("last_name")
        @NotBlank(message = "Last name is required")
        String lastName,

        @JsonProperty("tx_ref")
        @NotBlank(message = "Transaction reference is required")
        String txRef,

        @JsonProperty("callback_url")
        @NotBlank(message = "Callback URL is required")
        String callbackUrl,

        @JsonProperty("return_url")
        String returnUrl   // Optional
) {
}