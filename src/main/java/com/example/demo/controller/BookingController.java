package com.example.demo.controller;

import com.example.demo.enitity.Booking;
import com.example.demo.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // GET USER BOOKINGS
    @PostMapping("/checkout")
    public ResponseEntity<Booking> createBooking(
            @RequestParam String userId,
            @RequestParam String receiveDate,
            @RequestParam String returnDate,
            @RequestParam String location
    ) {
        return ResponseEntity.ok(bookingService.createInitialBooking(userId, receiveDate, returnDate, location));
    }
    @GetMapping
    public ResponseEntity<List<Booking>> getAllUserBookings(
            @RequestParam String userId
    ) {

        System.out.println("USER ID: " + userId);

        List<Booking> bookings =
                bookingService.getBookingsByUserId(userId);

        return ResponseEntity.ok(bookings);
    }

    // UPDATE CHECKOUT INFO
    @PutMapping("/checkout/{id}")
    public ResponseEntity<Booking> updateCheckoutInfo(
            @PathVariable Long id,
            @RequestBody Booking updateInfo
    ) {

        Booking updatedBooking =
                bookingService.finalizeCheckout(id, updateInfo);

        return ResponseEntity.ok(updatedBooking);
    }
}