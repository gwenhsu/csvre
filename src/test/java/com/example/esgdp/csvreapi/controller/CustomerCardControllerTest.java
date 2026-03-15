package com.example.esgdp.csvreapi.controller;

import com.example.esgdp.csvreapi.dto.CustomerCardDto;
import com.example.esgdp.csvreapi.service.CustomerCardService;
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

@WebMvcTest(CustomerCardController.class)
class CustomerCardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerCardService service;

    private CustomerCardDto dto;

    @BeforeEach
    void setUp() {
        dto = new CustomerCardDto();
        dto.setCustCode("C001");
        dto.setCustShortname("TSMC");
        dto.setCustRegion("Asia");
        dto.setParticipateSinceYear(2022);
        dto.setCalenderSinceMonth(1);
        dto.setCalenderToMonth(12);
    }

    @Test
    @WithMockUser(username = "admin")
    void getAll_returnsOk() throws Exception {
        when(service.getAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/customer-card")
                        .header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].custCode").value("C001"))
                .andExpect(jsonPath("$[0].custShortname").value("TSMC"))
                .andExpect(jsonPath("$[0].custRegion").value("Asia"));
    }

    @Test
    @WithMockUser(username = "admin")
    void getAll_emptyList_returnsOk() throws Exception {
        when(service.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/customer-card")
                        .header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser(username = "admin")
    void getByCustCode_found_returnsOk() throws Exception {
        when(service.getByCustCode("C001")).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/customer-card/C001")
                        .header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.custCode").value("C001"))
                .andExpect(jsonPath("$.participateSinceYear").value(2022));
    }

    @Test
    @WithMockUser(username = "admin")
    void getByCustCode_notFound_returns404() throws Exception {
        when(service.getByCustCode("UNKNOWN")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/customer-card/UNKNOWN")
                        .header("login-user", "user01"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin")
    void create_returnsCreated() throws Exception {
        when(service.create(any(CustomerCardDto.class))).thenReturn(dto);

        mockMvc.perform(post("/api/customer-card")
                        .header("login-user", "user01")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.custCode").value("C001"))
                .andExpect(jsonPath("$.calenderSinceMonth").value(1))
                .andExpect(jsonPath("$.calenderToMonth").value(12));
    }

    @Test
    @WithMockUser(username = "admin")
    void update_found_returnsOk() throws Exception {
        dto.setParticipateSinceYear(2023);
        when(service.update(eq("C001"), any(CustomerCardDto.class))).thenReturn(Optional.of(dto));

        mockMvc.perform(put("/api/customer-card/C001")
                        .header("login-user", "user01")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.participateSinceYear").value(2023));
    }

    @Test
    @WithMockUser(username = "admin")
    void update_notFound_returns404() throws Exception {
        when(service.update(eq("UNKNOWN"), any(CustomerCardDto.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/customer-card/UNKNOWN")
                        .header("login-user", "user01")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_withoutAuth_returns401() throws Exception {
        mockMvc.perform(get("/api/customer-card")
                        .header("login-user", "user01"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin")
    void getByCustCode_withoutLoginUserHeader_returns400() throws Exception {
        mockMvc.perform(get("/api/customer-card/C001"))
                .andExpect(status().isBadRequest());
    }
}
