package com.example.demo.service;

import com.example.demo.exception.WebhookVerificationException;
import com.example.demo.model.dto.WebhookEventDto;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeWebhookService {
    
    private static final Logger logger = LoggerFactory.getLogger(StripeWebhookService.class);
    private static final String CHECKOUT_SESSION_COMPLETED = "checkout.session.completed";
    private static final String PAYMENT_STATUS_PAID = "paid";
    
    @Value("${stripe.webhook.secret}")
    private String webhookSecret;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private EmailService emailService;
    
    public void processWebhookEvent(String payload, String sigHeader) {
        Event event = verifyAndConstructEvent(payload, sigHeader);
        
        if (CHECKOUT_SESSION_COMPLETED.equals(event.getType())) {
            handleCheckoutSessionCompleted(event);
        } else {
            logger.debug("Ignoring event type: {}", event.getType());
        }
    }
    
    private Event verifyAndConstructEvent(String payload, String sigHeader) {
        try {
            return Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            logger.error("Webhook signature verification failed");
            throw new WebhookVerificationException("Invalid webhook signature", e);
        }
    }
    
    private void handleCheckoutSessionCompleted(Event event) {
        Session session = deserializeSession(event);
        WebhookEventDto eventDto = extractSessionData(session);
        
        if (!PAYMENT_STATUS_PAID.equals(eventDto.getPaymentStatus())) {
            logger.info("Payment not completed, status: {}", eventDto.getPaymentStatus());
            return;
        }
        
        logger.info("Processing completed payment for session");
        orderService.createOrder(eventDto);
        
        if (eventDto.getEmail() != null) {
            emailService.sendOrderConfirmation(eventDto.getEmail(), eventDto.getSessionId());
        } else {
            logger.warn("No email provided, skipping confirmation email");
        }
    }
    
    private Session deserializeSession(Event event) {
        try {
            return (Session) event.getDataObjectDeserializer().deserializeUnsafe();
        } catch (Exception e) {
            logger.error("Failed to deserialize session from event");
            throw new RuntimeException("Session deserialization failed", e);
        }
    }
    
    private WebhookEventDto extractSessionData(Session session) {
        String email = session.getCustomerDetails() != null 
            ? session.getCustomerDetails().getEmail() 
            : null;
        Double amount = session.getAmountTotal() != null 
            ? session.getAmountTotal() / 100.0 
            : 0.0;
        
        return WebhookEventDto.builder()
            .sessionId(session.getId())
            .email(email)
            .amount(amount)
            .paymentStatus(session.getPaymentStatus())
            .build();
    }
}
