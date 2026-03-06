package com.example.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GalleryItemDto {
    private Integer id;
    private String title;
    private String imageUrl;
    private Boolean available;
}
