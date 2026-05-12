package com.example.demo.controller;

import com.example.demo.enitity.Booking;
import com.example.demo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*") // Allows your Next.js frontend to connect
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // 1. CREATE: This receives the data from your Zustand CheckoutStore
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        try {
            Booking saved = bookingService.createOrder(booking);
            // Using the builder pattern instead of the constructor
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            // Explicitly return an empty body with the error status
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 2. GET ALL: For Admin dashboard


    // 3. GET BY USER: For the User's "My Rentals" page
    // Maps to your useAuthStore's userId (String UUID)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getBookingsByUser(@PathVariable String userId) {
        List<Booking> userBookings = bookingService.getOrdersByUser(userId);
        return ResponseEntity.ok(userBookings);
    }
}