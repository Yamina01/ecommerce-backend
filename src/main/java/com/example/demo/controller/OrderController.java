package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.OrderDTO;
import com.example.demo.dto.OrderItemDTO;
import com.example.demo.entity.OrderEntity;
import com.example.demo.entity.OrderEntity.OrderStatus;
import com.example.demo.entity.OrderItemEntity;
import com.example.demo.entity.Userentity;
import com.example.demo.repo.Userrepo;
import com.example.demo.service.MockPaymentService;
import com.example.demo.service.OrderService;
import com.example.demo.service.PaymentService;
import com.paypal.base.rest.PayPalRESTException;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private Userrepo userRepo;
    
    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private MockPaymentService mockPaymentService;
    
    // Checkout endpoint
    @PostMapping("/checkout")
    public ResponseEntity<OrderDTO> checkout(
            @AuthenticationPrincipal(expression = "username") String email) {
        
        Userentity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        OrderEntity order = orderService.createOrderFromCart(user);
        OrderDTO orderDTO = convertToDTO(order);
        
        return ResponseEntity.ok(orderDTO);
    }
    
    // Get user's order history
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getUserOrders(
            @AuthenticationPrincipal(expression = "username") String email) {
        
        Userentity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<OrderEntity> orders = orderService.getUserOrders(user.getId());
        List<OrderDTO> orderDTOs = orders.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(orderDTOs);
    }
    
    // Get specific order details
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long orderId) {
        
        Userentity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        OrderEntity order = orderService.getUserOrder(user.getId(), orderId);
        OrderDTO orderDTO = convertToDTO(order);
        
        return ResponseEntity.ok(orderDTO);
    }

    // Cancel order
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long orderId) {
        
        Userentity user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        OrderEntity order = orderService.getUserOrder(user.getId(), orderId);
        
        if (order.getStatus() == OrderStatus.PENDING || order.getStatus() == OrderStatus.PROCESSING) {
            orderService.updateOrderStatus(orderId, OrderStatus.CANCELLED);
            return ResponseEntity.ok("Order cancelled successfully");
        } else {
            return ResponseEntity.badRequest().body("Cannot cancel order in current status: " + order.getStatus());
        }
    }

    // ========== DTO CONVERSION METHODS ==========
    
    private OrderDTO convertToDTO(OrderEntity order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setOrderDate(order.getOrderDate());
        
        if (order.getItems() != null) {
            List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(this::convertItemToDTO)
                .collect(Collectors.toList());
            dto.setItems(itemDTOs);
        }
        
        return dto;
    }
    
    private OrderItemDTO convertItemToDTO(OrderItemEntity item) {
        OrderItemDTO itemDTO = new OrderItemDTO();
        itemDTO.setId(item.getId());
        
        // ✅ FIXED: Include the entire product object
        itemDTO.setProduct(item.getProduct());
        
        // Also include flattened fields for safety
        if (item.getProduct() != null) {
            itemDTO.setProductId(item.getProduct().getId());
            itemDTO.setProductName(item.getProduct().getProductname());
            itemDTO.setProductPrice(item.getProduct().getPrice());
            itemDTO.setProductImageUrl(item.getProduct().getImageUrl());
        } else {
            // Fallback values
            itemDTO.setProductName("Product not available");
            itemDTO.setProductPrice(0.0);
        }
        
        itemDTO.setQuantity(item.getQuantity());
        itemDTO.setPriceAtPurchase(item.getPriceAtPurchase());
        
        Double itemTotal = (item.getPriceAtPurchase() != null ? item.getPriceAtPurchase() : 
                           (item.getProduct() != null ? item.getProduct().getPrice() : 0.0)) * item.getQuantity();
        itemDTO.setItemTotal(itemTotal);
        
        return itemDTO;
    }
}