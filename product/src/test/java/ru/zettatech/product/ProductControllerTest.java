package ru.zettatech.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.zettatech.product.controller.ProductController;
import ru.zettatech.product.filter.ProductFilter;
import ru.zettatech.product.model.Category;
import ru.zettatech.product.model.Manufacturer;
import ru.zettatech.product.model.Product;
import ru.zettatech.product.service.ProductService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetProducts() throws Exception {
        Product product = createTestProduct();
        product.setId(1L);

        Page<Product> page = new PageImpl<>(List.of(product), PageRequest.of(0, 10), 1);

        when(service.findAll(any(ProductFilter.class))).thenReturn(page);

        mockMvc.perform(get("/products")
                        .param("page", "0")
                        .param("limit", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void testGetProductById() throws Exception {
        Product product = createTestProduct();
        product.setId(1L);

        when(service.findById(1L)).thenReturn(product);

        mockMvc.perform(get("/products/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(product)));
    }

    @Test
    void testPostProduct() throws Exception {
        Product product = createTestProduct();
        product.setId(1L);

        Product savedProduct = createTestProduct();
        savedProduct.setId(1L);

        when(service.save(any(Product.class))).thenReturn(savedProduct);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(product)));
    }

    @Test
    public void putProductExists() throws Exception {
        Long id = 1L;

        Product product = createTestProduct();
        product.setId(id);

        when(service.existsById(id)).thenReturn(true);
        when(service.save(any(Product.class))).thenReturn(product);

        mockMvc.perform(put("/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(product)));
    }

    @Test
    public void putProductNotExist() throws Exception {
        Long id = 1L;
        Product product = createTestProduct();
        when(service.existsById(id)).thenReturn(false);

        mockMvc.perform(put("/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isNotModified())
                .andExpect(content().string(""));
    }

    @Test
    void testDeleteProduct() throws Exception {
        doNothing().when(service).deleteById(1L);

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isOk());
    }

    private Product createTestProduct() {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(1L);
        manufacturer.setName("Sony");

        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Phones");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Electronic");

        LocalDate localDate = LocalDate.of(2025, 9, 20);
        Date dateAdded = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Product product = new Product(
                10000,
                12345,
                "http://example.com/image.jpg",
                "Samsung Galaxy A23",
                "This is amsung Galaxy A23",
                manufacturer,
                BigDecimal.valueOf(500),
                BigDecimal.valueOf(0.5),
                (byte)1,
                1709,
                dateAdded
        );

        product.getCategories().add(category1);
        product.getCategories().add(category2);

        return product;
    }
}
