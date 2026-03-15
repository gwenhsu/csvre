package com.example.esgdp.csvreapi.dto;

import com.example.esgdp.csvreapi.model.ReUsage;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReUsageDto {
    private String fabCode;
    private String location;
    private Integer year;
    private Integer month;
    private BigDecimal usage;

    public static ReUsageDto from(ReUsage e) {
        ReUsageDto dto = new ReUsageDto();
        dto.fabCode = e.getFab().getFabCode();
        dto.location = e.getFab().getLocation();
        dto.year = e.getYear();
        dto.month = e.getMonth();
        dto.usage = e.getUsage();
        return dto;
    }
}
