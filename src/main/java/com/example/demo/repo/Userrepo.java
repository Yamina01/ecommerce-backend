package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Userentity;

@Repository
public interface Userrepo extends JpaRepository<Userentity, Long> {
    Optional<Userentity> findByEmail(String email);
}

