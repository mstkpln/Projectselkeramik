package com.example.demo.service;

import com.example.demo.model.dto.CheckoutRequest;
import com.example.demo.model.entity.ProductEntity;
import com.example.demo.repository.ProductRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StripeCheckoutService {
    
    private static final Logger logger = LoggerFactory.getLogger(StripeCheckoutService.class);
    
    @Value("${stripe.success.url}")
    private String successUrl;
    
    @Value("${stripe.cancel.url}")
    private String cancelUrl;
    
    @Autowired
    private ProductRepository productRepository;
    
    public String createCheckoutSession(CheckoutRequest request) {
        try {
            List<SessionCreateParams.LineItem> lineItems = buildLineItems(request);
            
            SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addAllLineItem(lineItems)
                .build();
            
            Session session = Session.create(params);
            logger.info("Checkout session created successfully");
            return session.getUrl();
        } catch (StripeException e) {
            logger.error("Failed to create checkout session", e);
            throw new RuntimeException("Failed to create checkout session", e);
        }
    }
    
    private List<SessionCreateParams.LineItem> buildLineItems(CheckoutRequest request) {
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
        
        for (CheckoutRequest.CheckoutItem item : request.getItems()) {
            ProductEntity product = productRepository.findById(item.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getProductId()));
            
            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setPriceData(
                    SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("usd")
                        .setUnitAmount((long) (product.getPrice() * 100))
                        .setProductData(
                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(product.getName())
                                .build()
                        )
                        .build()
                )
                .setQuantity(item.getQuantity().longValue())
                .build();
            
            lineItems.add(lineItem);
        }
        
        return lineItems;
    }
}
