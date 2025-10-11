package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "carts")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private Userentity user; 
    
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItemEntity> items = new ArrayList<>();
    
   
    public CartEntity() {
        // Default constructor required by JPA
    }
    
    // Parameterized constructor
    public CartEntity(Userentity user) {
        this.user = user;
    }
    
    // Helper method to add item to the cart
    public void addItem(ProductEntity product, Integer quantity) {
        CartItemEntity item = new CartItemEntity(product, quantity, this);
        items.add(item);
    }

    // Helper method to remove item from the cart
    public void removeItem(Long productId) {
        items.removeIf(item -> item.getProduct().getId().equals(productId));
    }
    
    // Calculate total price
    public Double getTotalPrice() {
        return items.stream()
                   .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                   .sum();
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Userentity getUser() {
        return user;
    }

    public void setUser(Userentity user) {
        this.user = user;
    }

    public List<CartItemEntity> getItems() {
        return items;
    }

    public void setItems(List<CartItemEntity> items) {
        this.items = items;
    }
}
