package com.example.demo.controller;

import com.example.demo.model.dto.CheckoutRequest;
import com.example.demo.service.StripeCheckoutService;
import com.example.demo.service.StripeWebhookService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stripe")
public class StripeWebhookController {
    
    private static final Logger logger = LoggerFactory.getLogger(StripeWebhookController.class);
    
    @Autowired
    private StripeWebhookService webhookService;
    
    @Autowired
    private StripeCheckoutService checkoutService;
    
    @PostMapping("/create-checkout-session")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@Valid @RequestBody CheckoutRequest request) {
        logger.info("Creating checkout session for {} items", request.getItems().size());
        String sessionUrl = checkoutService.createCheckoutSession(request);
        return ResponseEntity.ok(Map.of("url", sessionUrl));
    }
    
    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeEvent(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        
        logger.info("Received Stripe webhook event");
        webhookService.processWebhookEvent(payload, sigHeader);
        return ResponseEntity.ok("Event processed successfully");
    }
}
