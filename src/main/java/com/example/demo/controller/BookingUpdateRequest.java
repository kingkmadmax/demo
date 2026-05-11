package com.example.demo.controller;

public record BookingUpdateRequest(
        String rentalEndDate,
        String status,
        String paymentMethod,
        String paymentStatus,
        String address,
        String phone
) {}