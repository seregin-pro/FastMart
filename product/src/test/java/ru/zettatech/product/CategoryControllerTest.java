package ru.zettatech.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.zettatech.product.controller.CategoryController;
import ru.zettatech.product.filter.CategoryFilter;
import ru.zettatech.product.model.Category;
import ru.zettatech.product.service.CategoryService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetCategorys() throws Exception {
        Category category = createTestCategory();
        category.setId(1L);

        Page<Category> page = new PageImpl<>(List.of(category), PageRequest.of(0, 10), 1);

        when(service.findAll(any(CategoryFilter.class))).thenReturn(page);

        mockMvc.perform(get("/categories")
                        .param("page", "0")
                        .param("limit", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void testGetCategoryById() throws Exception {
        Category category = createTestCategory();
        category.setId(1L);

        when(service.findById(1L)).thenReturn(category);

        mockMvc.perform(get("/categories/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(category)));
    }

    @Test
    void testPostCategory() throws Exception {
        Category category = createTestCategory();
        category.setId(1L);

        Category savedCategory = createTestCategory();
        savedCategory.setId(1L);

        when(service.save(any(Category.class))).thenReturn(savedCategory);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(category)));
    }

    @Test
    public void putCategoryExists() throws Exception {
        Long id = 1L;

        Category category = createTestCategory();
        category.setId(id);

        when(service.existsById(id)).thenReturn(true);
        when(service.save(any(Category.class))).thenReturn(category);

        mockMvc.perform(put("/categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(category)));
    }

    @Test
    public void putCategoryNotExist() throws Exception {
        Long id = 1L;
        Category category = createTestCategory();
        when(service.existsById(id)).thenReturn(false);

        mockMvc.perform(put("/categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isNotModified())
                .andExpect(content().string(""));
    }

    @Test
    void testDeleteCategory() throws Exception {
        doNothing().when(service).deleteById(1L);

        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isOk());
    }

    private Category createTestCategory() {
        return new Category(
                "http://example.com/category.jpg",
                "Phones",
                0L,
                (byte)1
        );
    }
}