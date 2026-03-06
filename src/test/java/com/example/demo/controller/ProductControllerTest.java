package com.example.demo.controller;

import com.example.demo.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private ProductService productService;

    @Test
    @WithMockUser
    void getProducts_ReturnsListOfProducts() throws Exception {
        when(productService.getAllActiveProducts()).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getProducts_AllItemsSoldOut() throws Exception {
        when(productService.getAllActiveProducts()).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getProducts_ReturnsCorrectContentType() throws Exception {
        when(productService.getAllActiveProducts()).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }
}
