package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="order_items")
public class OrderItemEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "order_id",nullable = false)
	private OrderEntity order;
	


	@ManyToOne
	@JoinColumn(name = "product_id" , nullable = false)
	private ProductEntity product;

	private Integer quantity;
	private Double priceAtPurchase;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public OrderEntity getOrder() {
		return order;
	}
	public void setOrder(OrderEntity order) {
		this.order = order;
	}
	public ProductEntity getProduct() {
		return product;
	}
	public void setProduct(ProductEntity product) {
		this.product = product;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Double getPriceAtPurchase() {
		return priceAtPurchase;
	}
	public void setPriceAtPurchase(Double priceAtPurchase) {
		this.priceAtPurchase = priceAtPurchase;
	}
	/**
	 * @param id
	 * @param order
	 * @param product
	 * @param quantity
	 * @param priceAtPurchase
	 */
	public OrderItemEntity(Long id, OrderEntity order, ProductEntity product, Integer quantity,
			Double priceAtPurchase) {
		super();
		this.id = id;
		this.order = order;
		this.product = product;
		this.quantity = quantity;
		this.priceAtPurchase = priceAtPurchase;
	}
	/**
	 * 
	 */
	public OrderItemEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
