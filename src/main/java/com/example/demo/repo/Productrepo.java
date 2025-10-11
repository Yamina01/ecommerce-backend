package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.ProductEntity;
@Repository
public interface Productrepo extends JpaRepository<ProductEntity , Long>{

}
