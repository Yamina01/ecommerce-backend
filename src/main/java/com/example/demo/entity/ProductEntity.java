package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "products")
public class ProductEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column (nullable = false , unique = true)
	@NotBlank(message = "Productname is required")
	private String productname;
	
	@Column (nullable = false)
	@Positive(message = "Price must be greater than zero")
	private Double price;
	
	@Column (nullable = false)
	@Min(value= 0,message = "Stock cannot be negative")
	private Integer stock;
	
	@Column(nullable= false , length = 500)
	private String productDescription;
	

    @Column(name = "image_url")
    private String imageUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

	public ProductEntity(Long id, @NotBlank(message = "Productname is required") String productname,
			@Positive(message = "Price must be greater than zero") Double price,
			@Min(value = 0, message = "Stock cannot be negative") Integer stock, String productDescription , String imageUrl) {
		super();
		this.id = id;
		this.productname = productname;
		this.price = price;
		this.stock = stock;
		this.productDescription = productDescription;
		this.imageUrl = imageUrl;
	}

	/**
	 * 
	 */
	public ProductEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
}
