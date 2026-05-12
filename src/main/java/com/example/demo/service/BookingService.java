package com.example.demo.service;

import com.example.demo.enitity.Booking;
import com.example.demo.enitity.RentalProduct;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RentalRepository productRepository;

    @Transactional
    public Booking createOrder(Booking booking) {
        System.out.println("Attempting to save booking for product: " + booking.getProduct().getId());
        RentalProduct managedProduct = productRepository.findById(booking.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        booking.setProduct(managedProduct);
        Booking saved = bookingRepository.save(booking);
        System.out.println("Saved successfully with ID: " + saved.getId());
        return saved;




    }

    @Transactional(readOnly = true)
    public List<Booking> getAllOrders() {
        return bookingRepository.findAll();
    }


    @Transactional(readOnly = true)
    public List<Booking> getOrdersByUser(String userId) {
        return bookingRepository.findByUserId(userId);
    }
}