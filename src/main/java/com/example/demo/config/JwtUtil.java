package com.example.demo.config;

import java.util.Date;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
    private String secret = "iurtyufdgfdc45yt9485utoirhfireut958hutiyi45yht";
    private long expiration = 1000 * 60 * 60; // 1 hour

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email) // ← CRITICAL: Use setSubject for the email
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject(); // ← MUST match setSubject()
    }

//    public boolean validateToken(String token, String email) {
//        return (email.equals(extractUsername(token)) && !isTokenExpired(token));
//    }
    public boolean validateToken(String token, String email) {
        try {
            String extractedEmail = extractUsername(token);
            boolean emailMatches = email.equals(extractedEmail);
            boolean notExpired = !isTokenExpired(token);
            
            System.out.println("🔍 Validating token:");
            System.out.println("   Expected email: " + email);
            System.out.println("   Extracted email: " + extractedEmail);
            System.out.println("   Email matches: " + emailMatches);
            System.out.println("   Token not expired: " + notExpired);
            
            return emailMatches && notExpired;
        } catch (Exception e) {
            System.out.println("❌ Token validation error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}

	 
 



