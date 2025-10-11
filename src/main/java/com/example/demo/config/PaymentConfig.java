package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.paypal.base.rest.APIContext;

@Configuration
public class PaymentConfig {
    
    @Value("${paypal.client-id:sb}") // Added default value "sb"
    private String clientId;
    
    @Value("${paypal.client-secret:sb}") // Added default value "sb"
    private String clientSecret;
    
    @Value("${paypal.mode:sandbox}") // Added default value "sandbox"
    private String mode;
    
    @Bean // ← ADD THIS ANNOTATION
    public APIContext apiContext() {
        return new APIContext(clientId, clientSecret, mode);
    }
}