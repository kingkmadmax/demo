package com.example.demo.controller;

import com.example.demo.enitity.CartItem;
import com.example.demo.service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public List<CartItem> getCart(@RequestParam String userId) {
        return cartService.getCart(userId);
    }

    @PostMapping("/add")
    @Transactional
    public CartItem addToCart(@RequestBody CartRequest request) {
        return cartService.addToCart(
                request.userId(),
                request.productId(),
                request.name(),
                request.price(),
                request.image(),
                request.quantity(),
                request.deposit()
        );
    }

    @PutMapping("/update")
    public CartItem updateQuantity(@RequestBody UpdateRequest request) {
        return cartService.updateQuantity(request.cartItemId(), request.quantity());
    }

    @DeleteMapping("/{id}")
    public void removeItem(@PathVariable Long id) {
        cartService.removeFromCart(id);
    }

    // ==================== CHECKOUT ENDPOINT ====================
    @PostMapping("/checkout")                    // ← Must be POST
    public ResponseEntity<String> checkout(@RequestBody CheckoutRequest request) {
        try {
            cartService.checkout(request.userId(), request.rentalStartDate());
            return ResponseEntity.ok("Checkout successful! Items moved to Booking.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Checkout failed: " + e.getMessage());
        }
    }

    @DeleteMapping("/clear")
    public void clearCart(@RequestParam String userId) {
        cartService.clearCart(userId);
    }
}

// ==================== DTOs ====================
record CartRequest(
        String userId,
        Long productId,
        String name,
        double price,
        String image,
        int quantity,
        Double deposit
) {}

record UpdateRequest(
        Long cartItemId,
        int quantity
) {}

record CheckoutRequest(
        String userId,
        String rentalStartDate
) {}