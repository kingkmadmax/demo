package com.example.demo.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChapaResponse {
    private String status;
    private String message;
    private ResponseData data;

    @Data
    public static class ResponseData {
        @JsonProperty("checkout_url")
        private String checkoutUrl;

        @JsonProperty("tx_ref")
        private String txRef;
    }
}