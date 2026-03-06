package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Async
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public void sendOrderConfirmation(String toEmail, String sessionId) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Order Confirmation - Selkeramik");
            message.setText(buildEmailBody(sessionId));
            
            mailSender.send(message);
            logger.info("Order confirmation email sent successfully");
        } catch (Exception e) {
            logger.error("Failed to send confirmation email after retries", e);
        }
    }
    
    private String buildEmailBody(String sessionId) {
        return String.format(
            "Thank you for your order!\n\n" +
            "Your order has been confirmed.\n" +
            "Order Reference: %s\n\n" +
            "We will process your order shortly.\n\n" +
            "Best regards,\n" +
            "Selkeramik Team",
            maskSessionId(sessionId)
        );
    }
    
    private String maskSessionId(String sessionId) {
        if (sessionId == null || sessionId.length() < 8) {
            return "****";
        }
        return sessionId.substring(0, 4) + "****" + sessionId.substring(sessionId.length() - 4);
    }
}
