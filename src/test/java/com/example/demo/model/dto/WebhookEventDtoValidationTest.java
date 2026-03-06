package com.example.demo.model.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WebhookEventDtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validDto_NoViolations() {
        WebhookEventDto dto = WebhookEventDto.builder()
                .sessionId("cs_test_123")
                .email("test@example.com")
                .amount(99.99)
                .paymentStatus("paid")
                .build();

        Set<ConstraintViolation<WebhookEventDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void blankSessionId_HasViolation() {
        WebhookEventDto dto = WebhookEventDto.builder()
                .sessionId("")
                .email("test@example.com")
                .amount(99.99)
                .paymentStatus("paid")
                .build();

        Set<ConstraintViolation<WebhookEventDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Session ID is required"));
    }

    @Test
    void invalidEmail_HasViolation() {
        WebhookEventDto dto = WebhookEventDto.builder()
                .sessionId("cs_test_123")
                .email("invalid-email")
                .amount(99.99)
                .paymentStatus("paid")
                .build();

        Set<ConstraintViolation<WebhookEventDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Invalid email format"));
    }

    @Test
    void negativeAmount_HasViolation() {
        WebhookEventDto dto = WebhookEventDto.builder()
                .sessionId("cs_test_123")
                .email("test@example.com")
                .amount(-10.0)
                .paymentStatus("paid")
                .build();

        Set<ConstraintViolation<WebhookEventDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Amount must be positive"));
    }

    @Test
    void nullEmail_NoViolation() {
        WebhookEventDto dto = WebhookEventDto.builder()
                .sessionId("cs_test_123")
                .email(null)
                .amount(99.99)
                .paymentStatus("paid")
                .build();

        Set<ConstraintViolation<WebhookEventDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}
