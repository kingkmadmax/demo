package com.example.demo.controller;

import com.example.demo.enitity.Booking;
import com.example.demo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:3000") // Allows your Next.js frontend to connect
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
    @PutMapping("/{id}/accept")
    public ResponseEntity<String> acceptBooking(@PathVariable Long id) {
        boolean success = bookingService.acceptBooking(id);
        if (success) {
            return ResponseEntity.ok("Booking has been accepted successfully.");
        } else {
            return ResponseEntity.status(404).body("Booking ID not found.");
        }
    }
    @PutMapping("/{id}/decline")
    public ResponseEntity<Void> decline(@PathVariable Long id) {
        return bookingService.declineBooking(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

    // URL: DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return bookingService.deleteBooking(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }


    @GetMapping("/owner/me")
    public ResponseEntity<List<Booking>> getMyProductBookings() {
        String ownerId = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        List<Booking> bookings = bookingService.getBookingsByProductOwner(ownerId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getBookingsByUser(@PathVariable String userId) {
        List<Booking> userBookings = bookingService.getOrdersByUser(userId);
        return ResponseEntity.ok(userBookings);
    }
}