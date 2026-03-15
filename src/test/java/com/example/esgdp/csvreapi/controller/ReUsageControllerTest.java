package com.example.esgdp.csvreapi.controller;

import com.example.esgdp.csvreapi.model.Fab;
import com.example.esgdp.csvreapi.model.ReUsage;
import com.example.esgdp.csvreapi.service.ReUsageService;
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

@WebMvcTest(ReUsageController.class)
class ReUsageControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private ReUsageService service;

    private Fab fab;
    private ReUsage reUsage;

    @BeforeEach
    void setUp() {
        fab = new Fab(1L, "FAB1", "Taiwan");
        reUsage = new ReUsage(1L, fab, 2024, 1, new BigDecimal("800.0000"));
    }

    @Test
    @WithMockUser
    void getAll_returnsOk() throws Exception {
        when(service.getAll()).thenReturn(List.of(reUsage));

        mockMvc.perform(get("/api/re-usage").header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fabCode").value("FAB1"))
                .andExpect(jsonPath("$[0].year").value(2024));
    }

    @Test
    @WithMockUser
    void getAll_filterByFabId() throws Exception {
        when(service.getByFab(1L)).thenReturn(List.of(reUsage));

        mockMvc.perform(get("/api/re-usage?fabId=1").header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser
    void getAll_filterByFabIdAndYear() throws Exception {
        when(service.getByFabAndYear(1L, 2024)).thenReturn(List.of(reUsage));

        mockMvc.perform(get("/api/re-usage?fabId=1&year=2024").header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser
    void getAll_filterByFabIdYearMonth() throws Exception {
        when(service.getByFabYearMonth(1L, 2024, 1)).thenReturn(Optional.of(reUsage));

        mockMvc.perform(get("/api/re-usage?fabId=1&year=2024&month=1").header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser
    void getById_found() throws Exception {
        when(service.getById(1L)).thenReturn(Optional.of(reUsage));

        mockMvc.perform(get("/api/re-usage/1").header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fabCode").value("FAB1"));
    }

    @Test
    @WithMockUser
    void getById_notFound() throws Exception {
        when(service.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/re-usage/99").header("login-user", "user01"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void create_returnsCreated() throws Exception {
        when(service.create(any(ReUsage.class))).thenReturn(reUsage);

        mockMvc.perform(post("/api/re-usage")
                        .header("login-user", "user01")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reUsage)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.year").value(2024));
    }

    @Test
    @WithMockUser
    void update_found() throws Exception {
        when(service.update(eq(1L), any(ReUsage.class))).thenReturn(Optional.of(reUsage));

        mockMvc.perform(put("/api/re-usage/1")
                        .header("login-user", "user01")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reUsage)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void update_notFound() throws Exception {
        when(service.update(eq(99L), any(ReUsage.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/re-usage/99")
                        .header("login-user", "user01")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reUsage)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void delete_found() throws Exception {
        when(service.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/re-usage/1")
                        .header("login-user", "user01").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void delete_notFound() throws Exception {
        when(service.delete(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/re-usage/99")
                        .header("login-user", "user01").with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_withoutAuth_returns401() throws Exception {
        mockMvc.perform(get("/api/re-usage").header("login-user", "user01"))
                .andExpect(status().isUnauthorized());
    }
}
