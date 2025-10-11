package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.MockPaymentService;
import com.example.demo.service.OrderService;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private MockPaymentService mockPaymentService;

    // Mock payment creation
    @PostMapping("/mock-create")
    public String createMockPayment(@RequestParam Long orderId,
                                   @RequestParam Double amount) {
        return mockPaymentService.createMockPayment(orderId, amount);
    }
    
    // Mock success callback
    @GetMapping("/mock-success")
    public String mockPaymentSuccess(@RequestParam Long orderId) {
        orderService.updateOrderStatus(orderId, "PAID");
        return "✅ Mock payment successful! Order #" + orderId + " is now paid.";
    }
    
    // Mock cancel
    @GetMapping("/mock-cancel") 
    public String mockPaymentCancel(@RequestParam Long orderId) {
        return "❌ Mock payment cancelled for Order #" + orderId;
    }
}
