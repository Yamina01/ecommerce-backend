package com.example.demo.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        
        String path = request.getRequestURI();
        
        // Public paths that don't require authentication
        return path.equals("/api/users/signup") || 
               path.equals("/api/users/login") ||
               path.startsWith("/api/users/public-test") ||
               path.startsWith("/images/") || 
               path.startsWith("/css/") || 
               path.startsWith("/js/") ||
               path.startsWith("/api/products/");
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("JWT Filter - Processing: " + request.getRequestURI());
        
        final String authHeader = request.getHeader("Authorization");
        System.out.println("Auth Header present: " + (authHeader != null));

        String token = null;
        String email = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                email = jwtUtil.extractUsername(token);
                System.out.println("Extracted email: " + email);
            } catch (Exception e) {
                System.out.println("JWT extraction error: " + e.getMessage());
            }
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println("Attempting authentication for: " + email);
            
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                System.out.println("User details loaded: " + userDetails.getUsername());
                
                if (jwtUtil.validateToken(token, email)) {
                    System.out.println("Token validated successfully");
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Authentication SUCCESSFUL for: " + email);
                } else {
                    System.out.println("Token validation failed");
                }
            } catch (Exception e) {
                System.out.println("UserDetailsService error: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("No email or already authenticated");
        }
        
        filterChain.doFilter(request, response);
    }
}