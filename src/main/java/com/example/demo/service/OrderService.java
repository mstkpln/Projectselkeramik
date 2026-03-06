package com.example.demo.service;

import com.example.demo.model.dto.WebhookEventDto;
import com.example.demo.model.entity.OrderEntity;
import com.example.demo.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Transactional
    public OrderEntity createOrder(WebhookEventDto eventDto) {
        if (orderRepository.existsBySessionId(eventDto.getSessionId())) {
            logger.warn("Order already exists for session, skipping duplicate");
            return orderRepository.findBySessionId(eventDto.getSessionId()).orElseThrow();
        }
        
        OrderEntity order = new OrderEntity(
            eventDto.getSessionId(),
            eventDto.getEmail(),
            eventDto.getAmount()
        );
        
        OrderEntity savedOrder = orderRepository.save(order);
        logger.info("Order created successfully with ID: {}", savedOrder.getId());
        return savedOrder;
    }
}
