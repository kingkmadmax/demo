package com.example.demo.service;

import com.example.demo.enitity.Booking;
import com.example.demo.enitity.CartItem;
import com.example.demo.enitity.RentalProduct;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // ← Added for logging
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j // ← This annotation creates the 'log' object automatically
public class CartService {

    private final CartRepository cartRepository;
    private final BookingRepository bookingRepository;
    private final RentalRepository rentalRepository;

    public CartItem addToCart(String userId, Long productId, String name,
                              double price, String image, int quantity, Double deposit) {

        log.info("🛒 Adding product ID: {} to cart for user: {}", productId, userId);

        if (deposit == null) {
            log.debug("Deposit missing, fetching from rental repository for product: {}", productId);
            RentalProduct product = rentalRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));
            deposit = product.getDeposit();
        }

        CartItem cartItem = new CartItem(userId, productId, name, price, image, quantity, deposit);
        CartItem savedItem = cartRepository.save(cartItem);
        log.info("✅ Successfully added {} to user {}'s cart", name, userId);
        return savedItem;
    }

    public void checkout(String userId, String rentalStartDate) {
        log.info("🚀 Starting checkout process for user: {}", userId);

        List<CartItem> cartItems = cartRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {
            log.warn("⚠️ Checkout failed: Cart is empty for user: {}", userId);
            throw new RuntimeException("Cart is empty");
        }

        log.info("📦 Found {} items in cart. Converting to bookings...", cartItems.size());

        for (CartItem cart : cartItems) {
            Booking booking = new Booking();

            booking.setUserId(cart.getUserId());
            booking.setProductId(cart.getProductId());
            booking.setProductName(cart.getName());
            booking.setPrice(cart.getPrice());
            booking.setQuantity(cart.getQuantity());
            booking.setTotalPrice(cart.getPrice() * cart.getQuantity());
            booking.setDeposit(cart.getDeposit());
            booking.setRentalStartDate(rentalStartDate);
            booking.setBookingDate(LocalDateTime.now());
            booking.setStatus("PENDING");

            bookingRepository.save(booking);
            log.debug("📝 Created booking for product: {}", cart.getName());
        }

        cartRepository.deleteAllByUserId(userId);
        log.info("🏁 Checkout complete. Cart cleared for user: {}", userId);
    }

    public List<CartItem> getCart(String userId) {
        log.info("🔍 Fetching cart items for user: {}", userId);
        return cartRepository.findByUserId(userId);
    }

    public CartItem updateQuantity(Long cartItemId, int quantity) {
        log.info("🔄 Updating quantity to {} for cart item ID: {}", quantity, cartItemId);
        CartItem item = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        item.setQuantity(quantity);
        return cartRepository.save(item);
    }

    public void removeFromCart(Long id) {
        log.info("🗑️ Removing item ID: {} from cart", id);
        cartRepository.deleteById(id);
    }

    public void clearCart(String userId) {
        log.info("🧹 Clearing all cart items for user: {}", userId);
        cartRepository.deleteAllByUserId(userId);
    }
}