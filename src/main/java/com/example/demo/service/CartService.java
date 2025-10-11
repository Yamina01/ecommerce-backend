package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.CartEntity;
import com.example.demo.entity.CartItemEntity;
import com.example.demo.entity.ProductEntity;
import com.example.demo.entity.Userentity;
import com.example.demo.repo.CartRepository;
import com.example.demo.repo.Productrepo;

@Service
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private Productrepo productRepo;
 
    public CartEntity getOrCreateCart(Userentity user) {
        // === ADD DEBUG LOGGING HERE ===
        System.out.println("🔍 Getting cart for user: " + user.getEmail() + " (ID: " + user.getId() + ")");
        
        Optional<CartEntity> cartOpt = cartRepository.findByUser(user);
        System.out.println("Cart found: " + cartOpt.isPresent());
        
        if (cartOpt.isPresent()) {
            CartEntity cart = cartOpt.get();
            System.out.println("✅ Using existing cart ID: " + cart.getId());
            return cart;
        } else {
            System.out.println("🆕 Creating new cart for user ID: " + user.getId());
            CartEntity newCart = new CartEntity(user);
            
            // Debug: Check if user is properly set before saving
            System.out.println("New cart user: " + (newCart.getUser() != null ? 
                newCart.getUser().getId() + " - " + newCart.getUser().getEmail() : "NULL"));
            
            CartEntity savedCart = cartRepository.save(newCart);
            System.out.println("✅ New cart created with ID: " + savedCart.getId());
            return savedCart;
        }
    }
    
    public CartEntity addToCart(Userentity user, Long productId, Integer quantity) {
        System.out.println("🛒 Adding to cart - User: " + user.getEmail() + ", Product ID: " + productId + ", Qty: " + quantity);
        
        ProductEntity product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        if (quantity > product.getStock()) {
            throw new RuntimeException("Not enough stock available");
        }
        
        CartEntity cart = getOrCreateCart(user);
        
        // Check if product already exists in cart
        Optional<CartItemEntity> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItemEntity item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            System.out.println("📦 Updated quantity to: " + item.getQuantity());
        } else {
            CartItemEntity newItem = new CartItemEntity();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setCart(cart);
            cart.getItems().add(newItem);
            System.out.println("🆕 Added new item: " + product.getProductname());
        }
        
        CartEntity savedCart = cartRepository.save(cart);
        System.out.println("💾 Cart saved with " + savedCart.getItems().size() + " items");
        return savedCart;
    }
    
    public CartEntity getCart(Userentity user) {
        System.out.println("📋 Getting cart for user: " + user.getEmail());
        return getOrCreateCart(user);
    }
    public CartEntity updateCartItem(Userentity user, Long itemId, Integer quantity) {
        System.out.println("✏️ UPDATE CART ITEM - User: " + user.getEmail() + ", Item ID: " + itemId + ", Qty: " + quantity);
        
        CartEntity cart = getOrCreateCart(user);
        
        // Find the item in the cart
        CartItemEntity item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));
        
        System.out.println("📦 Found item: " + item.getProduct().getProductname() + " (current qty: " + item.getQuantity() + ")");
        
        // Update quantity
        if (quantity <= 0) {
            // Remove item if quantity is 0 or negative
            cart.getItems().remove(item);
            System.out.println("🗑️ Item removed (quantity <= 0)");
        } else {
            // Check stock availability
            if (quantity > item.getProduct().getStock()) {
                throw new RuntimeException("Not enough stock available. Only " + item.getProduct().getStock() + " left.");
            }
            item.setQuantity(quantity);
            System.out.println("✅ Quantity updated to: " + quantity);
        }
        
        CartEntity savedCart = cartRepository.save(cart);
        System.out.println("💾 Cart saved with " + savedCart.getItems().size() + " items");
        return savedCart;
    }
    public CartEntity removeFromCart(Userentity user, Long productId) {
        System.out.println("Removing product " + productId + " from cart");
        CartEntity cart = getOrCreateCart(user);
        
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        
        return cartRepository.save(cart);
    }
    
    public void clearCart(Userentity user) {
        System.out.println(" Clearing cart for user: " + user.getEmail());
        CartEntity cart = getOrCreateCart(user);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}