package com.example.demo.entity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<OrderEntity , Long>{
	//Find all orders by userId
	List<OrderEntity> findByUserId(Long userId);

	   // Find specific order by user ID and order ID
    Optional<OrderEntity> findByIdAndUserId(Long orderId, Long userId);
}
