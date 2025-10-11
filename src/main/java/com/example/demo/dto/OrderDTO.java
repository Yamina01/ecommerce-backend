package com.example.demo.dto;
import java.util.Date;
import java.util.List;

import com.example.demo.entity.OrderEntity.OrderStatus;


public class OrderDTO {
 private Long id;
 private Double totalAmount;
 private OrderStatus status;
 private Date orderDate;
 private List<OrderItemDTO> items;

// Constructors
public OrderDTO() {}

public OrderDTO(Long id, Double totalAmount, OrderStatus status, Date orderDate, List<OrderItemDTO> items) {
    this.id = id;
    this.totalAmount = totalAmount;
    this.status = status;
    this.orderDate = orderDate;
    this.items = items;
}
// Getters and Setters
public Long getId() { return id; }
public void setId(Long id) { this.id = id; }

public Double getTotalAmount() { return totalAmount; }
public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

public OrderStatus getStatus() { return status; }
public void setStatus(OrderStatus status) { this.status = status; }

public Date getOrderDate() { return orderDate; }
public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

public List<OrderItemDTO> getItems() { return items; }
public void setItems(List<OrderItemDTO> items) { this.items = items; }
}