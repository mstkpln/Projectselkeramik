package com.example.demo.config;

import com.example.demo.model.entity.ProductEntity;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            productRepository.save(new ProductEntity(null, "Ivy", 45.00, "/images/PXL_20260226_214519920~2.jpg", 10, true));
            productRepository.save(new ProductEntity(null, "Handmade Vase", 65.00, "/images/FullSizeRender_377.jpg", 5, true));
            productRepository.save(new ProductEntity(null, "Clay Pot", 55.00, "/images/FullSizeRender_548.jpg", 8, true));
            productRepository.save(new ProductEntity(null, "Artisan Plate", 35.00, "/images/FullSizeRender_777.webp", 15, true));
            productRepository.save(new ProductEntity(null, "Ceramic Cup", 25.00, "/images/FullSizeRender_95.jpg", 20, true));
            productRepository.save(new ProductEntity(null, "Sculptural Form3", 25.00, "/images/Screenshot2026-02-031223199.jpg", 12, true));
            productRepository.save(new ProductEntity(null, "Mini Vase_4", 35.00, "/images/PXL_20260226_214200859.jpg", 7, true));
            productRepository.save(new ProductEntity(null, "Mini Vase_5", 25.00, "/images/PXL_20260226_214448697~2.jpg", 9, true));
            productRepository.save(new ProductEntity(null, "Mini Vase_6", 25.00, "/images/PXL_20260226_214407125.jpg", 11, true));
            productRepository.save(new ProductEntity(null, "Mini Vase_7", 20.00, "/images/PXL_20260226_214334053.jpg", 14, true));
            productRepository.save(new ProductEntity(null, "Mini Vase_8", 25.00, "/images/miniVase_8.jpg", 11, true));
            productRepository.save(new ProductEntity(null, "Mini Vase_Planted", 20.00, "/images/miniVase_planted.jpg", 14, true));
        }
    }
}
