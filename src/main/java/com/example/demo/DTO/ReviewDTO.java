package com.example.demo.DTO;

import java.time.LocalDateTime;

public record ReviewDTO(
        Long id,
        String author,
        String comment,
        Integer rating,
        LocalDateTime createdAt
) {}