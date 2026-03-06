package com.example.demo.controller;

import com.example.demo.exception.WebhookVerificationException;
import com.example.demo.service.StripeCheckoutService;
import com.example.demo.service.StripeWebhookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StripeWebhookController.class)
class StripeWebhookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StripeWebhookService webhookService;
    
    @MockBean
    private StripeCheckoutService checkoutService;

    @Test
    @WithMockUser
    void handleStripeEvent_Success() throws Exception {
        String payload = "{\"type\":\"checkout.session.completed\"}";
        String signature = "valid_signature";
        doNothing().when(webhookService).processWebhookEvent(any(), any());

        mockMvc.perform(post("/api/stripe/webhook")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Stripe-Signature", signature)
                .content(payload)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Event processed successfully"));

        verify(webhookService, times(1)).processWebhookEvent(payload, signature);
    }

    @Test
    @WithMockUser
    void handleStripeEvent_InvalidSignature_ReturnsBadRequest() throws Exception {
        String payload = "{\"type\":\"checkout.session.completed\"}";
        String invalidSignature = "invalid_signature";
        doThrow(new WebhookVerificationException("Invalid signature"))
                .when(webhookService).processWebhookEvent(any(), any());

        mockMvc.perform(post("/api/stripe/webhook")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Stripe-Signature", invalidSignature)
                .content(payload)
                .with(csrf()))
                .andExpect(status().isBadRequest());

        verify(webhookService, times(1)).processWebhookEvent(payload, invalidSignature);
    }

    @Test
    @WithMockUser
    void handleStripeEvent_MissingSignatureHeader_ReturnsInternalServerError() throws Exception {
        String payload = "{\"type\":\"checkout.session.completed\"}";

        mockMvc.perform(post("/api/stripe/webhook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
                .with(csrf()))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser
    void handleStripeEvent_ServiceThrowsException_ReturnsInternalServerError() throws Exception {
        String payload = "{\"type\":\"checkout.session.completed\"}";
        String signature = "valid_signature";
        doThrow(new RuntimeException("Service error"))
                .when(webhookService).processWebhookEvent(any(), any());

        mockMvc.perform(post("/api/stripe/webhook")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Stripe-Signature", signature)
                .content(payload)
                .with(csrf()))
                .andExpect(status().isInternalServerError());

        verify(webhookService, times(1)).processWebhookEvent(payload, signature);
    }
}
