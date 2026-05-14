package com.example.demo.service;

import com.example.demo.enitity.Booking;
import com.example.demo.enitity.ProductSituation;
import com.example.demo.enitity.RentalProduct;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

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

        managedProduct.setSituation(ProductSituation.RENTED);
        booking.setProduct(managedProduct);
        Booking saved = bookingRepository.save(booking);
        System.out.println("Saved successfully with ID: " + saved.getId());
        return saved;




    }
    public List<Booking> getBookingsByProductOwner(String ownerId) {
        return bookingRepository.findByProductOwnerId(ownerId);
    }
    public boolean acceptBooking(Long id) {
        Optional<Booking> bookingOptional = bookingRepository.findById(id);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            booking.setStatus("ACCEPTED");
            bookingRepository.save(booking);
            return true;
        }
        return false; // Returns false if ID doesn't exist
    }
    public boolean declineBooking(Long id) {
        return bookingRepository.findById(id).map(booking -> {
            booking.setStatus("DECLINED");
            bookingRepository.save(booking);
            return true;
        }).orElse(false);
    }

    // Delete record (Record is removed from DB)
    public boolean deleteBooking(Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return true;
        }
        return false;
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