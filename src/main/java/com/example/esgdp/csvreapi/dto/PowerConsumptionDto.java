package com.example.esgdp.csvreapi.dto;

import com.example.esgdp.csvreapi.model.PowerConsumption;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PowerConsumptionDto {
    private String fabCode;
    private String location;
    private Integer year;
    private Integer month;
    private BigDecimal power;

    public static PowerConsumptionDto from(PowerConsumption e) {
        PowerConsumptionDto dto = new PowerConsumptionDto();
        dto.fabCode = e.getFab().getFabCode();
        dto.location = e.getFab().getLocation();
        dto.year = e.getYear();
        dto.month = e.getMonth();
        dto.power = e.getPower();
        return dto;
    }
}
