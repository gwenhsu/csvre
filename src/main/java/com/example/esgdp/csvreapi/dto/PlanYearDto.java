package com.example.esgdp.csvreapi.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlanYearDto {
    private Integer year;
    private BigDecimal rate;
    private Boolean isShow;
}
