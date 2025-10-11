package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.ProductEntity;
import com.example.demo.repo.Productrepo;
import java.util.List;

import jakarta.validation.Valid;

@Service
public class ProductService {
	
	private final Productrepo productrepo;
	 public ProductService(Productrepo productrepo) {
	   this.productrepo = productrepo;	 
	 }
// Create
	 public ProductEntity addProduct(@Valid ProductEntity product) {
		 return productrepo.save(product);
	 }
//	 Read(Get all products)
	 public List<ProductEntity> getAllProducts(){
		 return productrepo.findAll();
	 }
//	 Read(Get product by ID)
  public ProductEntity getProductById(Long id) {
	  return productrepo.findById(id)
			  .orElseThrow(()-> new RuntimeException("Product not found with Id: "+id));
  }
//  UPDATE 
  public ProductEntity updateProduct(Long id , @Valid ProductEntity updatedProduct) {
	  ProductEntity existingProduct = productrepo.findById(id)
			  .orElseThrow(()-> new RuntimeException("Product not found with ID :" + id));
	  
	  existingProduct.setProductname(updatedProduct.getProductname());
	  existingProduct.setPrice(updatedProduct.getPrice());
	  existingProduct.setStock(updatedProduct.getStock());
	  existingProduct.setProductDescription(updatedProduct.getProductDescription());
	  
	  return productrepo.save(existingProduct);
  }
//  Delete
  public void deleteProduct(Long id) {
	  if (!productrepo.existsById(id)) {
		  throw new RuntimeException("Product not found with ID : "+ id);
	  }
	  productrepo.deleteById(id);
  }
}
