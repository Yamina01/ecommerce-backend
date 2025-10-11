package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.config.JwtUtil;
import com.example.demo.entity.Userentity;
import com.example.demo.repo.Userrepo;

import java.util.Optional;

@Service
public class Userservice {

    @Autowired
    private Userrepo userrepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    // Register (Signup)
    public Userentity registerUser(Userentity user) {
        if (userrepo.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }
        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userrepo.save(user);
    }

    // Login
    public String loginUser(String email, String password) {
        System.out.println("Login attempt: " + email);
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
            );

            if (authentication.isAuthenticated()) {
                System.out.println("Login successful for: " + email);
                return jwtUtil.generateToken(email);
            } else {
                System.out.println("Authentication failed (not authenticated)");
                throw new RuntimeException("Invalid login credentials");
            }
        } catch (Exception e) {
            System.out.println("Authentication exception: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }

    // Find user by email
    public Optional<Userentity> findByEmail(String email) {
        return userrepo.findByEmail(email);
    }

    //  Update user
    public Userentity updateUser(Userentity user) {
        return userrepo.save(user);
    }
}