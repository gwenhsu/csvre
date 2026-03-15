package com.example.esgdp.csvreapi.dto;

import com.example.esgdp.csvreapi.model.EnergyType;
import lombok.Data;

@Data
public class RePreferenceItemDto {
    private EnergyType type;
    private Integer priority;
}
