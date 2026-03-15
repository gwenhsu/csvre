package com.example.esgdp.csvreapi.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ThirdPartyDto {
    private String custCode;
    private String custShortname;
    private String custRegion;
    private BigDecimal revenue;
}
