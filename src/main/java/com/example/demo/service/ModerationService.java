package com.example.demo.service;

import com.example.demo.enitity.Renter;
import com.example.demo.enitity.UserEnitity;
import com.example.demo.repository.RenterRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ModerationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RenterRepository renterRepository;

    // --- FETCH ALL ---
    public List<UserEnitity> findAllUsers() {
        return userRepository.findAll();
    }

    public List<Renter> findAllRenters() {
        return renterRepository.findAll();
    }

    // --- UPDATE LOGIC ---
    public UserEnitity updateUser(String id, UserEnitity userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(userDetails.getUsername());
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            user.setEmail(userDetails.getEmail());
            user.setUpdatedAt(LocalDateTime.now());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public Renter updateRenter(String id, Renter renterDetails) {
        return renterRepository.findById(id).map(renter -> {
            renter.setFullName(renterDetails.getFullName());
            renter.setEmail(renterDetails.getEmail());
            renter.setRole(renterDetails.getRole());
            renter.setUpdatedAt(LocalDateTime.now());
            return renterRepository.save(renter);
        }).orElseThrow(() -> new RuntimeException("Renter not found with id: " + id));
    }

    // --- DELETE LOGIC ---
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public void deleteRenter(String id) {
        renterRepository.deleteById(id);
    }
}