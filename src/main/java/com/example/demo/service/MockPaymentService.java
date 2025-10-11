package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class MockPaymentService {
    
    public String createMockPayment(Long orderId, Double amount) {
        // Simulate payment processing
        System.out.println("💳 Mock payment processed for Order #" + orderId);
        System.out.println("💰 Amount: ₹" + amount);
        
        // Return a mock success response
        return "http://localhost:8080/api/payment/mock-success?orderId=" + orderId;
    }
}