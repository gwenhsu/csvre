package com.example.esgdp.csvreapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlanDto {
    private Integer option;
    private List<PlanYearDto> years;
}
