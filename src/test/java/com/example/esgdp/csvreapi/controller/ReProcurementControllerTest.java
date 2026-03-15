package com.example.esgdp.csvreapi.controller;

import com.example.esgdp.csvreapi.model.EnergyType;
import com.example.esgdp.csvreapi.model.ReProcurement;
import com.example.esgdp.csvreapi.service.ReProcurementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReProcurementController.class)
class ReProcurementControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private ReProcurementService service;

    private ReProcurement rp;

    @BeforeEach
    void setUp() {
        rp = new ReProcurement(1L, "TW", 2024, 1, 1, EnergyType.WIND, new BigDecimal("500.0000"));
    }

    @Test
    @WithMockUser
    void getAll_returnsOk() throws Exception {
        when(service.getAll()).thenReturn(List.of(rp));

        mockMvc.perform(get("/api/re-procurement").header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].region").value("TW"))
                .andExpect(jsonPath("$[0].year").value(2024))
                .andExpect(jsonPath("$[0].energyType").value("WIND"));
    }

    @Test
    @WithMockUser
    void getAll_filterByRegion() throws Exception {
        when(service.getByRegion("TW")).thenReturn(List.of(rp));

        mockMvc.perform(get("/api/re-procurement?region=TW").header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser
    void getAll_filterByRegionAndYear() throws Exception {
        when(service.getByRegionAndYear("TW", 2024)).thenReturn(List.of(rp));

        mockMvc.perform(get("/api/re-procurement?region=TW&year=2024").header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser
    void getAll_filterByRegionYearMonth() throws Exception {
        when(service.getByRegionYearMonth("TW", 2024, 1)).thenReturn(List.of(rp));

        mockMvc.perform(get("/api/re-procurement?region=TW&year=2024&month=1").header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser
    void getById_found() throws Exception {
        when(service.getById(1L)).thenReturn(Optional.of(rp));

        mockMvc.perform(get("/api/re-procurement/1").header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.region").value("TW"));
    }

    @Test
    @WithMockUser
    void getById_notFound() throws Exception {
        when(service.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/re-procurement/99").header("login-user", "user01"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void create_returnsCreated() throws Exception {
        when(service.create(any(ReProcurement.class))).thenReturn(rp);

        mockMvc.perform(post("/api/re-procurement")
                        .header("login-user", "user01")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rp)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.energyType").value("WIND"));
    }

    @Test
    @WithMockUser
    void update_found() throws Exception {
        when(service.update(eq(1L), any(ReProcurement.class))).thenReturn(Optional.of(rp));

        mockMvc.perform(put("/api/re-procurement/1")
                        .header("login-user", "user01")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rp)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void update_notFound() throws Exception {
        when(service.update(eq(99L), any(ReProcurement.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/re-procurement/99")
                        .header("login-user", "user01")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rp)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void delete_found() throws Exception {
        when(service.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/re-procurement/1")
                        .header("login-user", "user01").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void delete_notFound() throws Exception {
        when(service.delete(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/re-procurement/99")
                        .header("login-user", "user01").with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_withoutAuth_returns401() throws Exception {
        mockMvc.perform(get("/api/re-procurement").header("login-user", "user01"))
                .andExpect(status().isUnauthorized());
    }
}
