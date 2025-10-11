package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {
	@PostMapping("/paypal")
	public String handlePayPalWebhook(@RequestBody String payload) {
		System.out.println("Received PayPal webhook:"+ payload);
		return "Webhook received";
	}

}
