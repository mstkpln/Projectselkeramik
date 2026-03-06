package com.example.demo.service;

import com.example.demo.model.dto.ProductDto;
import com.example.demo.model.entity.ProductEntity;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Cacheable("products")
    public List<ProductDto> getAllActiveProducts() {
        return productRepository.findByActiveTrue().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
    
    private ProductDto toDto(ProductEntity entity) {
        return new ProductDto(
            entity.getId().intValue(),
            entity.getName(),
            entity.getPrice(),
            entity.getImageUrl(),
            entity.getStock()
        );
    }
}
