package com.example.demo.service;

import com.example.demo.model.dto.WebhookEventDto;
import com.example.demo.model.entity.OrderEntity;
import com.example.demo.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private WebhookEventDto testEventDto;

    @BeforeEach
    void setUp() {
        testEventDto = WebhookEventDto.builder()
                .sessionId("cs_test_123456")
                .email("test@example.com")
                .amount(99.99)
                .paymentStatus("paid")
                .build();
    }

    @Test
    void createOrder_Success() {
        // Arrange
        when(orderRepository.existsBySessionId(testEventDto.getSessionId())).thenReturn(false);
        OrderEntity savedOrder = new OrderEntity(testEventDto.getSessionId(), testEventDto.getEmail(), testEventDto.getAmount());
        savedOrder.setId(1L);
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedOrder);

        // Act
        OrderEntity result = orderService.createOrder(testEventDto);

        // Assert
        assertNotNull(result);
        assertEquals(testEventDto.getSessionId(), result.getSessionId());
        assertEquals(testEventDto.getEmail(), result.getEmail());
        assertEquals(testEventDto.getAmount(), result.getAmount());
        verify(orderRepository, times(1)).existsBySessionId(testEventDto.getSessionId());
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }

    @Test
    void createOrder_DuplicateOrder_ReturnsExisting() {
        // Arrange
        OrderEntity existingOrder = new OrderEntity(testEventDto.getSessionId(), testEventDto.getEmail(), testEventDto.getAmount());
        existingOrder.setId(1L);
        when(orderRepository.existsBySessionId(testEventDto.getSessionId())).thenReturn(true);
        when(orderRepository.findBySessionId(testEventDto.getSessionId())).thenReturn(Optional.of(existingOrder));

        // Act
        OrderEntity result = orderService.createOrder(testEventDto);

        // Assert
        assertNotNull(result);
        assertEquals(existingOrder.getId(), result.getId());
        verify(orderRepository, times(1)).existsBySessionId(testEventDto.getSessionId());
        verify(orderRepository, never()).save(any(OrderEntity.class));
    }

    @Test
    void createOrder_WithNullEmail() {
        // Arrange
        testEventDto.setEmail(null);
        when(orderRepository.existsBySessionId(testEventDto.getSessionId())).thenReturn(false);
        OrderEntity savedOrder = new OrderEntity(testEventDto.getSessionId(), null, testEventDto.getAmount());
        when(orderRepository.save(any(OrderEntity.class))).thenReturn(savedOrder);

        // Act
        OrderEntity result = orderService.createOrder(testEventDto);

        // Assert
        assertNotNull(result);
        assertNull(result.getEmail());
        verify(orderRepository, times(1)).save(any(OrderEntity.class));
    }
}
