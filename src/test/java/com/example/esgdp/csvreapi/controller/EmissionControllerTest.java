package com.example.esgdp.csvreapi.controller;

import com.example.esgdp.csvreapi.model.Emission;
import com.example.esgdp.csvreapi.model.EmissionGoal;
import com.example.esgdp.csvreapi.model.Fab;
import com.example.esgdp.csvreapi.service.EmissionGoalService;
import com.example.esgdp.csvreapi.service.EmissionService;
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

@WebMvcTest(EmissionController.class)
class EmissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmissionService emissionService;

    @MockBean
    private EmissionGoalService emissionGoalService;

    private Fab fab;
    private Emission emission;

    @BeforeEach
    void setUp() {
        fab = new Fab(1L, "FAB1", "Taiwan");
        emission = new Emission(1L, fab, 2024, 1,
                new BigDecimal("100.0000"),
                new BigDecimal("200.0000"),
                new BigDecimal("50.0000"),
                new BigDecimal("30.0000"));
    }

    // ── Emission ──────────────────────────────────────────

    @Test
    @WithMockUser(username = "admin")
    void getAll_returnsOk() throws Exception {
        when(emissionService.getAll()).thenReturn(List.of(emission));

        mockMvc.perform(get("/api/emission")
                        .header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fabCode").value("FAB1"))
                .andExpect(jsonPath("$[0].year").value(2024))
                .andExpect(jsonPath("$[0].month").value(1));
    }

    @Test
    @WithMockUser(username = "admin")
    void getAll_withFabFilter_returnsFiltered() throws Exception {
        when(emissionService.getByFab(1L)).thenReturn(List.of(emission));

        mockMvc.perform(get("/api/emission?fabId=1")
                        .header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(username = "admin")
    void getById_found_returnsOk() throws Exception {
        when(emissionService.getById(1L)).thenReturn(Optional.of(emission));

        mockMvc.perform(get("/api/emission/1")
                        .header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fabCode").value("FAB1"));
    }

    @Test
    @WithMockUser(username = "admin")
    void getById_notFound_returns404() throws Exception {
        when(emissionService.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/emission/99")
                        .header("login-user", "user01"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin")
    void create_returnsCreated() throws Exception {
        when(emissionService.create(any(Emission.class))).thenReturn(emission);

        mockMvc.perform(post("/api/emission")
                        .header("login-user", "user01")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emission)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.year").value(2024));
    }

    @Test
    @WithMockUser(username = "admin")
    void update_found_returnsOk() throws Exception {
        when(emissionService.update(eq(1L), any(Emission.class))).thenReturn(Optional.of(emission));

        mockMvc.perform(put("/api/emission/1")
                        .header("login-user", "user01")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emission)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.month").value(1));
    }

    @Test
    @WithMockUser(username = "admin")
    void update_notFound_returns404() throws Exception {
        when(emissionService.update(eq(99L), any(Emission.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/emission/99")
                        .header("login-user", "user01")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emission)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_withoutAuth_returns401() throws Exception {
        mockMvc.perform(get("/api/emission")
                        .header("login-user", "user01"))
                .andExpect(status().isUnauthorized());
    }

    // ── Emission Goal ─────────────────────────────────────

    @Test
    @WithMockUser(username = "admin")
    void getGoals_returnsOk() throws Exception {
        EmissionGoal goal = new EmissionGoal(1L, 2024,
                new BigDecimal("100"), new BigDecimal("200"),
                new BigDecimal("50"), new BigDecimal("30"));
        when(emissionGoalService.getAll()).thenReturn(List.of(goal));

        mockMvc.perform(get("/api/emission/goal")
                        .header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].year").value(2024));
    }

    @Test
    @WithMockUser(username = "admin")
    void getGoalById_notFound_returns404() throws Exception {
        when(emissionGoalService.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/emission/goal/99")
                        .header("login-user", "user01"))
                .andExpect(status().isNotFound());
    }
}
