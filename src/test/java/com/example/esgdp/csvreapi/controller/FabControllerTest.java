package com.example.esgdp.csvreapi.controller;

import com.example.esgdp.csvreapi.model.Fab;
import com.example.esgdp.csvreapi.service.FabService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FabController.class)
class FabControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private FabService fabService;

    private Fab fab;

    @BeforeEach
    void setUp() {
        fab = new Fab(1L, "FAB1", "Taiwan");
    }

    @Test
    @WithMockUser
    void getAll_returnsOk() throws Exception {
        when(fabService.getAll()).thenReturn(List.of(fab));

        mockMvc.perform(get("/api/fab").header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fabCode").value("FAB1"))
                .andExpect(jsonPath("$[0].location").value("Taiwan"));
    }

    @Test
    @WithMockUser
    void getById_found() throws Exception {
        when(fabService.getById(1L)).thenReturn(Optional.of(fab));

        mockMvc.perform(get("/api/fab/1").header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fabCode").value("FAB1"));
    }

    @Test
    @WithMockUser
    void getById_notFound() throws Exception {
        when(fabService.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/fab/99").header("login-user", "user01"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void getByFabCode_found() throws Exception {
        when(fabService.getByFabCode("FAB1")).thenReturn(Optional.of(fab));

        mockMvc.perform(get("/api/fab/code/FAB1").header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.location").value("Taiwan"));
    }

    @Test
    @WithMockUser
    void getByFabCode_notFound() throws Exception {
        when(fabService.getByFabCode("UNKNOWN")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/fab/code/UNKNOWN").header("login-user", "user01"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void create_returnsCreated() throws Exception {
        when(fabService.create(any(Fab.class))).thenReturn(fab);

        mockMvc.perform(post("/api/fab")
                        .header("login-user", "user01")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fab)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fabCode").value("FAB1"));
    }

    @Test
    @WithMockUser
    void update_found() throws Exception {
        when(fabService.update(eq(1L), any(Fab.class))).thenReturn(Optional.of(fab));

        mockMvc.perform(put("/api/fab/1")
                        .header("login-user", "user01")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fab)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fabCode").value("FAB1"));
    }

    @Test
    @WithMockUser
    void update_notFound() throws Exception {
        when(fabService.update(eq(99L), any(Fab.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/fab/99")
                        .header("login-user", "user01")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fab)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void delete_found() throws Exception {
        when(fabService.delete(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/fab/1")
                        .header("login-user", "user01")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void delete_notFound() throws Exception {
        when(fabService.delete(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/fab/99")
                        .header("login-user", "user01")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_withoutAuth_returns401() throws Exception {
        mockMvc.perform(get("/api/fab").header("login-user", "user01"))
                .andExpect(status().isUnauthorized());
    }
}
