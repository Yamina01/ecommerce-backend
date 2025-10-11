package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Service
public class PaymentService {
    
    @Autowired
    private APIContext apiContext;
    
    public String createPayment(Double total, String currency, String method, 
            String intent, String description, 
            String cancelUrl, String successUrl) throws PayPalRESTException {

Amount amount = new Amount();
amount.setCurrency(currency);
amount.setTotal(String.format("%.2f", total));

Transaction transaction = new Transaction();
transaction.setDescription(description);
transaction.setAmount(amount);

List<Transaction> transactions = new ArrayList<>();
transactions.add(transaction);

Payer payer = new Payer();
payer.setPaymentMethod(method);

Payment payment = new Payment();
payment.setIntent(intent);
payment.setPayer(payer);
payment.setTransactions(transactions);

RedirectUrls redirectUrls = new RedirectUrls();
redirectUrls.setCancelUrl(cancelUrl);
redirectUrls.setReturnUrl(successUrl);
payment.setRedirectUrls(redirectUrls);

// Create payment and get the response
Payment createdPayment = payment.create(apiContext);

// Extract the approval URL from the payment response
for (Links link : createdPayment.getLinks()) {
if (link.getRel().equals("approval_url")) {
return link.getHref(); // Return the URL string
}
}

throw new RuntimeException("No approval URL found in PayPal response");
}
    
    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        
        return payment.execute(apiContext, paymentExecution);
    }
}