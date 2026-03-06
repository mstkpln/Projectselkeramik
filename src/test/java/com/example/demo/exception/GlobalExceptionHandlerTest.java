package com.example.demo.exception;

import com.example.demo.model.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleWebhookVerificationException_ReturnsBadRequest() {
        // Arrange
        WebhookVerificationException exception = new WebhookVerificationException("Invalid signature");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/stripe/webhook");

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleWebhookVerificationException(exception, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid webhook signature", response.getBody().getMessage());
        assertEquals("/api/stripe/webhook", response.getBody().getPath());
    }

    @Test
    void handleGenericException_ReturnsInternalServerError() {
        // Arrange
        Exception exception = new RuntimeException("Unexpected error");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception, request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("An unexpected error occurred", response.getBody().getMessage());
    }

    @Test
    void handleGenericException_WithNullPointerException() {
        // Arrange
        NullPointerException exception = new NullPointerException("Null value");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception, request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
    }
    
    @Test
    void handleIllegalArgumentException_ReturnsBadRequest() {
        // Arrange
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");

        // Act
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleIllegalArgumentException(exception, request);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid argument", response.getBody().getMessage());
    }
}
