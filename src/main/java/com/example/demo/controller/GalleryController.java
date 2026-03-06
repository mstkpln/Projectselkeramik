package com.example.demo.controller;

import com.example.demo.model.dto.GalleryItemDto;
import com.example.demo.model.entity.ProductEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class GalleryController {
    
    @GetMapping("/gallery")
    public List<GalleryItemDto> getGalleryItems() {
        return Arrays.asList(
            new GalleryItemDto(1, "Ivy", "/images/FullSizeRender_238.jpg", false),
            new GalleryItemDto(2, "Handmade Vase", "/images/FullSizeRender_377.jpg", false),
            new GalleryItemDto(3, "Clay Pot", "/images/FullSizeRender_548.jpg", false),
            new GalleryItemDto(4, "Artisan Plate", "/images/FullSizeRender_777.webp", false),
            new GalleryItemDto(5, "Ceramic Cup", "/images/FullSizeRender_95.jpg", false),
            new GalleryItemDto(6, "Sculptural Form3", "/images/Screenshot2026-02-031223199.jpg", false),
            new GalleryItemDto(7, "Mini Vase_4", "/images/PXL_20260226_214200859.jpg", false),
            new GalleryItemDto(8, "Mini Vase_5", "/images/PXL_20260226_214448697~2.jpg", false),
            new GalleryItemDto(9, "Mini Vase_6", "/images/PXL_20260226_214407125.jpg", false),
            new GalleryItemDto(10, "Mini Vase_7", "/images/PXL_20260226_214334053.jpg", false),
            new GalleryItemDto(11, "Mini Vase_8","/images/miniVase_8.jpg", false ),
            new GalleryItemDto(12, "Mini Vase_Planted","/images/miniVase_planted.jpg", false )
        );
    }
}
