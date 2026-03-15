package com.example.esgdp.csvreapi.controller;

import com.example.esgdp.csvreapi.model.Fab;
import com.example.esgdp.csvreapi.model.PowerFactor;
import com.example.esgdp.csvreapi.service.PowerFactorService;
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

@WebMvcTest(PowerFactorController.class)
class PowerFactorControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private PowerFactorService service;

    private Fab fab;
    private PowerFactor pf;

    @BeforeEach
    void setUp() {
        fab = new Fab(1L, "FAB1", "Taiwan");
        pf = new PowerFactor(1L, fab, 2024, new BigDecimal("0.5000"));
    }

    @Test
    @WithMockUser
    void getAll_returnsOk() throws Exception {
        when(service.getAll()).thenReturn(List.of(pf));

        mockMvc.perform(get("/api/power-factor").header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fabCode").value("FAB1"))
                .andExpect(jsonPath("$[0].year").value(2024));
    }

    @Test
    @WithMockUser
    void getAll_filterByFabId() throws Exception {
        when(service.getByFab(1L)).thenReturn(List.of(pf));

        mockMvc.perform(get("/api/power-factor?fabId=1").header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser
    void getAll_filterByFabIdAndYear() throws Exception {
        when(service.getByFabAndYear(1L, 2024)).thenReturn(Optional.of(pf));

        mockMvc.perform(get("/api/power-factor?fabId=1&year=2024").header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser
    void getById_found() throws Exception {
        when(service.getById(1L)).thenReturn(Optional.of(pf));

        mockMvc.perform(get("/api/power-factor/1").header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fabCode").value("FAB1"));
    }

    @Test
    @WithMockUser
    void getById_notFound() throws Exception {
        when(service.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/power-factor/99").header("login-user", "user01"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void create_returnsCreated() throws Exception {
        when(service.create(any(PowerFactor.class))).thenReturn(pf);

        mockMvc.perform(post("/api/power-factor")
                        .header("login-user", "user01")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pf)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.year").value(2024));
    }

    @Test
    @WithMockUser
    void update_found() throws Exception {
        when(service.update(eq(1L), any(PowerFactor.class))).thenReturn(Optional.of(pf));

        mockMvc.perform(put("/api/power-factor/1")
                        .header("login-user", "user01")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pf)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void update_notFound() throws Exception {
        when(service.update(eq(99L), any(PowerFactor.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/power-factor/99")
                        .header("login-user", "user01")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pf)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void delete_found() throws Exception {
        when(service.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/power-factor/1")
                        .header("login-user", "user01").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void delete_notFound() throws Exception {
        when(service.delete(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/power-factor/99")
                        .header("login-user", "user01").with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_withoutAuth_returns401() throws Exception {
        mockMvc.perform(get("/api/power-factor").header("login-user", "user01"))
                .andExpect(status().isUnauthorized());
    }
}
