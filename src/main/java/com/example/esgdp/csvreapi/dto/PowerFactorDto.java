package com.example.esgdp.csvreapi.dto;

import com.example.esgdp.csvreapi.model.PowerFactor;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PowerFactorDto {
    private String fabCode;
    private String location;
    private Integer year;
    private BigDecimal factor;

    public static PowerFactorDto from(PowerFactor e) {
        PowerFactorDto dto = new PowerFactorDto();
        dto.fabCode = e.getFab().getFabCode();
        dto.location = e.getFab().getLocation();
        dto.year = e.getYear();
        dto.factor = e.getFactor();
        return dto;
    }
}
