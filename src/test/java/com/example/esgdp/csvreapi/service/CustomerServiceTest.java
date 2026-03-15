package com.example.esgdp.csvreapi.service;

import com.example.esgdp.csvreapi.dto.CustomerDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerServiceTest {

    private final CustomerService service = new CustomerService();

    @Test
    void getAll_returnsHardcodedList() {
        List<CustomerDto> result = service.getAll();

        assertThat(result).hasSize(4);
    }

    @Test
    void getAll_containsExpectedCustomers() {
        List<CustomerDto> result = service.getAll();

        assertThat(result).extracting(CustomerDto::getCustCode)
                .containsExactlyInAnyOrder("C001", "C002", "C003", "C004");
    }

    @Test
    void getAll_allHaveRequiredFields() {
        List<CustomerDto> result = service.getAll();

        result.forEach(c -> {
            assertThat(c.getCustCode()).isNotBlank();
            assertThat(c.getCustShortname()).isNotBlank();
            assertThat(c.getCustRegion()).isNotBlank();
        });
    }
}
