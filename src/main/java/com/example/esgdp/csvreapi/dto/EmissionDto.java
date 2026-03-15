package com.example.esgdp.csvreapi.dto;

import com.example.esgdp.csvreapi.model.Emission;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EmissionDto {
    private String fabCode;
    private String location;
    private Integer year;
    private Integer month;
    private BigDecimal s1;
    private BigDecimal s2L;
    private BigDecimal s2M;
    private BigDecimal s3;

    public static EmissionDto from(Emission e) {
        EmissionDto dto = new EmissionDto();
        dto.fabCode = e.getFab().getFabCode();
        dto.location = e.getFab().getLocation();
        dto.year = e.getYear();
        dto.month = e.getMonth();
        dto.s1 = e.getS1();
        dto.s2L = e.getS2L();
        dto.s2M = e.getS2M();
        dto.s3 = e.getS3();
        return dto;
    }
}
