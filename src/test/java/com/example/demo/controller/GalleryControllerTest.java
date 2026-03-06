package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(GalleryController.class)
class GalleryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void getGalleryItems_ReturnsListOfItems() throws Exception {
        mockMvc.perform(get("/api/gallery"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Ivy")))
                .andExpect(jsonPath("$[0].imageUrl", is("/images/FullSizeRender_238.jpg")))
                .andExpect(jsonPath("$[0].available", is(false)));
    }

    @Test
    @WithMockUser
    void getGalleryItems_ReturnsCorrectContentType() throws Exception {
        mockMvc.perform(get("/api/gallery"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }
}
