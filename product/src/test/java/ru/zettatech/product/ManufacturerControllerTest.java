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
import ru.zettatech.product.controller.ManufacturerController;
import ru.zettatech.product.filter.ManufacturerFilter;
import ru.zettatech.product.model.Manufacturer;
import ru.zettatech.product.service.ManufacturerService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ManufacturerController.class)
public class ManufacturerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ManufacturerService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetManufacturers() throws Exception {
        Manufacturer manufacturer = createTestManufacturer();
        manufacturer.setId(1L);

        Page<Manufacturer> page = new PageImpl<>(List.of(manufacturer), PageRequest.of(0, 10), 1);

        when(service.findAll(any(ManufacturerFilter.class))).thenReturn(page);

        mockMvc.perform(get("/manufacturers")
                        .param("page", "0")
                        .param("limit", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void testGetManufacturerById() throws Exception {
        Manufacturer manufacturer = createTestManufacturer();
        manufacturer.setId(1L);

        when(service.findById(1L)).thenReturn(manufacturer);

        mockMvc.perform(get("/manufacturers/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(manufacturer)));
    }

    @Test
    void testPostManufacturer() throws Exception {
        Manufacturer manufacturer = createTestManufacturer();
        manufacturer.setId(1L);

        Manufacturer savedManufacturer = createTestManufacturer();
        savedManufacturer.setId(1L);

        when(service.save(any(Manufacturer.class))).thenReturn(savedManufacturer);

        mockMvc.perform(post("/manufacturers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(manufacturer))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(manufacturer)));
    }

    @Test
    public void putManufacturerExists() throws Exception {
        Long id = 1L;

        Manufacturer manufacturer = createTestManufacturer();
        manufacturer.setId(id);

        when(service.existsById(id)).thenReturn(true);
        when(service.save(any(Manufacturer.class))).thenReturn(manufacturer);

        mockMvc.perform(put("/manufacturers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(manufacturer)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(manufacturer)));
    }

    @Test
    public void putManufacturerNotExist() throws Exception {
        Long id = 1L;
        Manufacturer manufacturer = createTestManufacturer();
        when(service.existsById(id)).thenReturn(false);

        mockMvc.perform(put("/manufacturers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(manufacturer)))
                .andExpect(status().isNotModified())
                .andExpect(content().string(""));
    }

    @Test
    void testDeleteManufacturer() throws Exception {
        doNothing().when(service).deleteById(1L);

        mockMvc.perform(delete("/manufacturers/1"))
                .andExpect(status().isOk());
    }

    private Manufacturer createTestManufacturer() {
        return new Manufacturer(
                "http://example.com/sony.jpg",
                "Sony"
        );
    }
}
