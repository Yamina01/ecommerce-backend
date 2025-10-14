package com.example.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Userentity;
import com.example.demo.repo.Userrepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final Userrepo userRepo;

    public UserDetailsServiceImpl(Userrepo userRepo) {
        this.userRepo = userRepo;
    }

;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("🔐 UserDetailsService loading user: " + email);
        
        Userentity user = userRepo.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("❌ User not found: " + email);
                    return new UsernameNotFoundException("User not found with email: " + email);
                });
        
        System.out.println("✅ User found: " + user.getEmail() + ", password: " + user.getPassword());
        
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }
}
