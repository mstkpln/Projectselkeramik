package com.example.demo.model.dto;

import com.example.demo.validation.ValidPaymentStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookEventDto {
    
    @NotBlank(message = "Session ID is required")
    private String sessionId;
    
    @Email(message = "Invalid email format")
    private String email;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;
    
    @NotBlank(message = "Payment status is required")
    @ValidPaymentStatus
    private String paymentStatus;
}
