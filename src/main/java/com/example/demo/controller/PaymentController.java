package com.example.demo.controller;

import com.example.demo.DTO.PaymentRequest;
import com.example.demo.service.ChapaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final ChapaService chapaService;



    @PostMapping("/initialize")
    public ResponseEntity<String> initializePayment(@RequestBody PaymentRequest request) {
        String checkoutUrl = chapaService.initializePayment(request);
        return ResponseEntity.ok(checkoutUrl);
    }
}