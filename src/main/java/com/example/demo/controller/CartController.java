package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.CartEntity;
import com.example.demo.entity.Userentity;
import com.example.demo.repo.Userrepo;
import com.example.demo.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private Userrepo userRepository;

    @GetMapping
    public ResponseEntity<?> getCart(
            @AuthenticationPrincipal(expression = "username") String email) {
        
        System.out.println("🛒 GET CART - User: " + email);
        
        try {
            Userentity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            CartEntity cart = cartService.getCart(user);
            
            // Debug logging
            System.out.println("✅ Cart found - ID: " + cart.getId());
            System.out.println("📦 Items count: " + (cart.getItems() != null ? cart.getItems().size() : 0));
            
            if (cart.getItems() != null) {
                cart.getItems().forEach(item -> {
                    System.out.println("   - " + item.getProduct().getProductname() + " x " + item.getQuantity());
                });
            }
            
            return ResponseEntity.ok(cart);
            
        } catch (Exception e) {
            System.out.println("❌ Error getting cart: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @AuthenticationPrincipal(expression = "username") String email,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        
        System.out.println("🛒 ADD TO CART - User: " + email + ", Product: " + productId + ", Qty: " + quantity);
        
        try {
            Userentity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            CartEntity cart = cartService.addToCart(user, productId, quantity);
            
            System.out.println("✅ Item added successfully");
            System.out.println("📦 Cart now has " + cart.getItems().size() + " items");
            
            return ResponseEntity.ok(cart);
            
        } catch (Exception e) {
            System.out.println("❌ Error adding to cart: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateCartItem(
            @AuthenticationPrincipal(expression = "username") String email,
            @RequestParam Long itemId,
            @RequestParam Integer quantity) {
        
        System.out.println("🛒 UPDATE CART ITEM - User: " + email + ", Item: " + itemId + ", Qty: " + quantity);
        
        try {
            Userentity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            CartEntity cart = cartService.updateCartItem(user, itemId, quantity);
            
            System.out.println("✅ Item quantity updated successfully");
            return ResponseEntity.ok(cart);
            
        } catch (Exception e) {
            System.out.println("❌ Error updating cart item: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(
            @AuthenticationPrincipal(expression = "username") String email) {
        
        System.out.println("🛒 CLEAR CART - User: " + email);
        
        try {
            Userentity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            cartService.clearCart(user);
            
            System.out.println("✅ Cart cleared successfully");
            return ResponseEntity.ok("Cart cleared successfully");
            
        } catch (Exception e) {
            System.out.println("❌ Error clearing cart: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeFromCart(
            @AuthenticationPrincipal(expression = "username") String email,
            @PathVariable Long productId) {
        
        System.out.println("🛒 REMOVE FROM CART - User: " + email + ", Product: " + productId);
        
        try {
            Userentity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            CartEntity cart = cartService.removeFromCart(user, productId);
            
            System.out.println("✅ Item removed successfully");
            return ResponseEntity.ok(cart);
            
        } catch (Exception e) {
            System.out.println("❌ Error removing from cart: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}

