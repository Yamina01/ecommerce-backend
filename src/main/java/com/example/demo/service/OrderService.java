package com.example.demo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.CartEntity;
import com.example.demo.entity.CartItemEntity;
import com.example.demo.entity.OrderEntity;
import com.example.demo.entity.OrderEntity.OrderStatus;
import com.example.demo.entity.OrderItemEntity;
import com.example.demo.entity.OrderRepo;
import com.example.demo.entity.Userentity;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private CartService cartService;
   
    @Autowired
    private EmailService emailService;
    

    @Transactional
    public OrderEntity createOrderFromCart(Userentity user) {
        // Get user's cart
        CartEntity cart = cartService.getCart(user);
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot checkout empty cart");
        }
        
        // Create new order
        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING); // Use enum
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderDate(new Date());

        // Convert cart items to order items
        for (CartItemEntity cartItem : cart.getItems()) {
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(cartItem.getProduct().getPrice());
            order.addItem(orderItem);
        }

        // Save order
        OrderEntity savedOrder = orderRepo.save(order);
        
        // Send order confirmation email
        emailService.sendOrderConfirmation(savedOrder);


        // Clear the cart after successful order
        cartService.clearCart(user);

        return savedOrder;
    }

    public List<OrderEntity> getUserOrders(Long userId) {
        return orderRepo.findByUserId(userId);
    }

    public OrderEntity getUserOrder(Long userId, Long orderId) {
        return orderRepo.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // Admin methods
    public List<OrderEntity> getAllOrders() {
        return orderRepo.findAll();
    }

    public void updateOrderStatus(Long orderId, OrderStatus status) {
        OrderEntity order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        OrderStatus oldStatus = order.getStatus(); // Store old status for email
        order.setStatus(status);
        orderRepo.save(order);
        
        // Send status update email
        emailService.sendStatusUpdate(order, oldStatus, status);
    }

    // Overloaded method for String status (if needed for backward compatibility)
    public void updateOrderStatus(Long orderId, String status) {
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            updateOrderStatus(orderId, orderStatus);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid order status: " + status);
        }
    }
        // Special method for payment success (can send payment-specific email)
        public void processPaymentSuccess(Long orderId) {
            OrderEntity order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            
            OrderStatus oldStatus = order.getStatus();
            order.setStatus(OrderStatus.PAID);
            orderRepo.save(order);
            
            // Send payment success email
            emailService.sendPaymentSuccess(order);
        
    }
}

