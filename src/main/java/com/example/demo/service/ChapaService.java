package com.example.demo.service;

import com.example.demo.DTO.PaymentRequest;
import com.example.demo.DTO.PaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@Slf4j
public class ChapaService {

    private final RestClient restClient;

    @Value("${chapa.secret-key}")
    private String secretKey;

    public ChapaService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.chapa.co/v1")
                .build();
    }

    public String initializePayment(PaymentRequest request) {
        validateRequest(request);

        log.info("🚀 Initializing Chapa payment | TX_REF: {} | Amount: {} | Email: {}",
                request.txRef(), request.amount(), request.email());

        if (secretKey == null || secretKey.isBlank()) {
            log.error("❌ Chapa Secret Key is not configured!");
            throw new RuntimeException("Payment gateway configuration error");
        }

        try {
            PaymentResponse response = restClient.post()
                    .uri("/transaction/initialize")
                    .header("Authorization", "Bearer " + secretKey.trim())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                        String errorBody = new String(res.getBody().readAllBytes());
                        log.error("❌ Chapa API Error {}: {}", res.getStatusCode(), errorBody);
                        throw new RuntimeException("Chapa rejected the request: " + errorBody);
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                        log.error("❌ Chapa Server Error: {}", res.getStatusCode());
                        throw new RuntimeException("Chapa payment service is temporarily unavailable");
                    })
                    .body(PaymentResponse.class);

            log.info("✅ Chapa payment initialized successfully | Checkout URL generated for TX_REF: {}",
                    request.txRef());

            return response.data().checkoutUrl();

        } catch (Exception e) {
            log.error("❌ Failed to initialize payment for TX_REF: {} | Error: {}",
                    request.txRef(), e.getMessage(), e);
            throw new RuntimeException("Payment initialization failed: " + e.getMessage());
        }
    }

    private void validateRequest(PaymentRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Payment request cannot be null");
        }
        // Additional custom validation if needed beyond Jakarta annotations
        if (request.txRef() == null || request.txRef().isBlank()) {
            throw new IllegalArgumentException("Transaction reference (tx_ref) is required");
        }
    }
}