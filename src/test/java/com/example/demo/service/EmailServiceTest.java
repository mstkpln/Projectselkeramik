package com.example.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void sendOrderConfirmation_Success() {
        // Arrange
        String email = "customer@example.com";
        String sessionId = "cs_test_123456789";
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        // Act
        emailService.sendOrderConfirmation(email, sessionId);

        // Assert
        verify(mailSender, times(1)).send(messageCaptor.capture());
        SimpleMailMessage sentMessage = messageCaptor.getValue();
        
        assertNotNull(sentMessage);
        assertEquals(email, sentMessage.getTo()[0]);
        assertEquals("Order Confirmation - Selkeramik", sentMessage.getSubject());
        assertTrue(sentMessage.getText().contains("Thank you for your order"));
        assertTrue(sentMessage.getText().contains("cs_t****6789")); // Masked session ID
    }

    @Test
    void sendOrderConfirmation_MailSenderThrowsException() {
        String email = "customer@example.com";
        String sessionId = "cs_test_123";
        doThrow(new RuntimeException("SMTP error")).when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendOrderConfirmation(email, sessionId);
        
        verify(mailSender, atLeastOnce()).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendOrderConfirmation_WithShortSessionId() {
        // Arrange
        String email = "test@example.com";
        String sessionId = "short";
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        // Act
        emailService.sendOrderConfirmation(email, sessionId);

        // Assert
        verify(mailSender, times(1)).send(messageCaptor.capture());
        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertTrue(sentMessage.getText().contains("****")); // Should mask short IDs
    }
}
