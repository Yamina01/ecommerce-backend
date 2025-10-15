package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    
    @GetMapping("/health")
    public String health() {
        return "✅ Ecommerce Backend is RUNNING!";
    }
    
    @GetMapping("/")
    public String home() {
        return "🎉 Backend Deployed! Use /api/products for products";
    }
}