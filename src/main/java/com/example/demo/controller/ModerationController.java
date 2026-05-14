package com.example.demo.controller;

import com.example.demo.enitity.Renter;
import com.example.demo.enitity.UserEnitity;
import com.example.demo.service.ModerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moderation")
public class ModerationController {

    @Autowired
    private ModerationService moderationService;

    // --- GET ---
    @GetMapping("/users")
    public List<UserEnitity> getUsers() {
        return moderationService.findAllUsers();
    }

    @GetMapping("/renters")
    public List<Renter> getRenters() {
        return moderationService.findAllRenters();
    }

    // --- PUT (Update) ---
    @PutMapping("/users/{id}")
    public ResponseEntity<UserEnitity> updateUser(@PathVariable String id, @RequestBody UserEnitity user) {
        return ResponseEntity.ok(moderationService.updateUser(id, user));
    }

    @PutMapping("/renters/{id}")
    public ResponseEntity<Renter> updateRenter(@PathVariable String id, @RequestBody Renter renter) {
        return ResponseEntity.ok(moderationService.updateRenter(id, renter));
    }

    // --- DELETE ---
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        moderationService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/renters/{id}")
    public ResponseEntity<Void> deleteRenter(@PathVariable String id) {
        moderationService.deleteRenter(id);
        return ResponseEntity.ok().build();
    }
}