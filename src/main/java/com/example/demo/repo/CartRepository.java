package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.CartEntity;
import com.example.demo.entity.Userentity;

@Repository
public interface CartRepository extends JpaRepository<CartEntity,Long> {
Optional<CartEntity> findByUser(Userentity user);
Optional<CartEntity> findByUserId(Long userId);
}

