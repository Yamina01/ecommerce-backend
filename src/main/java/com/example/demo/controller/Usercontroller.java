package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.UserProfileDTO;
import com.example.demo.entity.Userentity;
import com.example.demo.service.Userservice;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class Usercontroller {

    @Autowired
    private Userservice userservice;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Signup
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Userentity user) {
        try {
            Userentity savedUser = userservice.registerUser(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Userentity user) {
        try {
            String token = userservice.loginUser(user.getEmail(), user.getPassword());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }

    // Get user profile
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal(expression = "username") String email) {
        try {
            System.out.println("Fetching profile for: " + email);
            
            Optional<Userentity> userOpt = userservice.findByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body("User not found");
            }
            
            Userentity user = userOpt.get();
            UserProfileDTO profileDTO = convertToProfileDTO(user);
            return ResponseEntity.ok(profileDTO);
            
        } catch (Exception e) {
            System.out.println("Error fetching profile: " + e.getMessage());
            return ResponseEntity.status(500).body("Error fetching profile");
        }
    }

    // Update user profile
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @AuthenticationPrincipal(expression = "username") String email,
            @RequestBody UserProfileDTO profileDTO) {
        try {
            System.out.println("Updating profile for: " + email);
            
            Optional<Userentity> userOpt = userservice.findByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body("User not found");
            }
            
            Userentity user = userOpt.get();
            user.setName(profileDTO.getName());
            user.setPhone(profileDTO.getPhone());
            
            Userentity updatedUser = userservice.updateUser(user);
            UserProfileDTO updatedDTO = convertToProfileDTO(updatedUser);
            
            return ResponseEntity.ok(updatedDTO);
            
        } catch (Exception e) {
            System.out.println("Error updating profile: " + e.getMessage());
            return ResponseEntity.status(500).body("Error updating profile");
        }
    }

    // Change password
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @AuthenticationPrincipal(expression = "username") String email,
            @RequestBody ChangePasswordRequest request) {
        try {
            System.out.println("Changing password for: " + email);
            
            Optional<Userentity> userOpt = userservice.findByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body("User not found");
            }
            
            Userentity user = userOpt.get();
            
            // Verify current password
            if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                return ResponseEntity.badRequest().body("Current password is incorrect");
            }
            
            // Update password
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userservice.updateUser(user);
            
            return ResponseEntity.ok("Password updated successfully");
            
        } catch (Exception e) {
            System.out.println("Error changing password: " + e.getMessage());
            return ResponseEntity.status(500).body("Error changing password");
        }
    }
 // Simple test - same as orders but for users
    @GetMapping("/test-simple")
    public ResponseEntity<?> testSimple(@AuthenticationPrincipal(expression = "username") String email) {
        System.out.println("Test simple - User: " + email);
        
        if (email == null) {
            return ResponseEntity.status(401).body("No user in token");
        }
        
        // Return simple success message
        return ResponseEntity.ok("Profile test successful for: " + email);
    }

    private UserProfileDTO convertToProfileDTO(Userentity user) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRegistrationDate(user.getRegistrationDate());
        return dto;
    }

    // DTO for password change request
    public static class ChangePasswordRequest {
        private String currentPassword;
        private String newPassword;

        public String getCurrentPassword() { return currentPassword; }
        public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}