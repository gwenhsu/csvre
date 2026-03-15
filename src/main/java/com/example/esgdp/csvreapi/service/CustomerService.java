package com.example.esgdp.csvreapi.service;

import com.example.esgdp.csvreapi.dto.CustomerDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    public List<CustomerDto> getAll() {
        // TODO: 換成真實外部 API
        // CustomerDto[] result = restTemplate.getForObject(customerUrl, CustomerDto[].class);
        // return result != null ? Arrays.asList(result) : List.of();

        CustomerDto c1 = new CustomerDto();
        c1.setCustCode("C001");
        c1.setCustShortname("TSMC");
        c1.setCustRegion("Asia");

        CustomerDto c2 = new CustomerDto();
        c2.setCustCode("C002");
        c2.setCustShortname("Samsung");
        c2.setCustRegion("Asia");

        CustomerDto c3 = new CustomerDto();
        c3.setCustCode("C003");
        c3.setCustShortname("Intel");
        c3.setCustRegion("USA");

        CustomerDto c4 = new CustomerDto();
        c4.setCustCode("C004");
        c4.setCustShortname("ASML");
        c4.setCustRegion("Europe");

        return List.of(c1, c2, c3, c4);
    }
}
