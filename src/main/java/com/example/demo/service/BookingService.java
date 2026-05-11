package com.example.demo.service;

import com.example.demo.enitity.Booking;
import com.example.demo.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    // GET BOOKINGS
    public List<Booking> getBookingsByUserId(String userId) {

        System.out.println("SEARCHING BOOKINGS FOR: " + userId);

        List<Booking> bookings =
                bookingRepository.findByUserId(userId);

        System.out.println("FOUND BOOKINGS: " + bookings.size());

        return bookings;
    }

    // FINALIZE CHECKOUT
    @Transactional
    public Booking finalizeCheckout(
            Long id,
            Booking updateInfo
    ) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        booking.setName(updateInfo.getName());
        booking.setEmail(updateInfo.getEmail());
        booking.setPhone(updateInfo.getPhone());
        booking.setLocation(updateInfo.getLocation());
        booking.setFid(updateInfo.getFid());
        // Parse the ISO strings from the frontend into LocalDates
        if (updateInfo.getReceiveDate() != null) {
            booking.setReceiveDate(updateInfo.getReceiveDate());
        }
        if (updateInfo.getReturnDate() != null) {
            booking.setReturnDate(updateInfo.getReturnDate());
        }
        booking.setFaceImage(updateInfo.getFaceImage());
        booking.setIdImage(updateInfo.getIdImage());

        booking.setReceiveDate(updateInfo.getReceiveDate());
        booking.setReturnDate(updateInfo.getReturnDate());

        booking.setStatus("CONFIRMED");

        return bookingRepository.save(booking);
    }
    @Transactional
    public Booking createInitialBooking(String userId, String receiveDate, String returnDate, String location) {
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setReceiveDate(LocalDate.parse(receiveDate));
        booking.setReturnDate(LocalDate.parse(returnDate));
        booking.setLocation(location);
        booking.setStatus("PENDING");

        return bookingRepository.save(booking);
    }
}