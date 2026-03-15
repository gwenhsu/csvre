package com.example.esgdp.csvreapi.controller;

import com.example.esgdp.csvreapi.dto.CustomerDto;
import com.example.esgdp.csvreapi.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private CustomerService service;

    @Test
    @WithMockUser
    void getAll_returnsOk() throws Exception {
        CustomerDto c1 = new CustomerDto();
        c1.setCustCode("C001");
        c1.setCustShortname("TSMC");
        c1.setCustRegion("Asia");

        CustomerDto c2 = new CustomerDto();
        c2.setCustCode("C002");
        c2.setCustShortname("Samsung");
        c2.setCustRegion("Asia");

        when(service.getAll()).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/api/customer").header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].custCode").value("C001"))
                .andExpect(jsonPath("$[0].custShortname").value("TSMC"))
                .andExpect(jsonPath("$[1].custCode").value("C002"));
    }

    @Test
    @WithMockUser
    void getAll_emptyList() throws Exception {
        when(service.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/customer").header("login-user", "user01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getAll_withoutAuth_returns401() throws Exception {
        mockMvc.perform(get("/api/customer").header("login-user", "user01"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void getAll_withoutLoginUserHeader_returns400() throws Exception {
        mockMvc.perform(get("/api/customer"))
                .andExpect(status().isBadRequest());
    }
}
