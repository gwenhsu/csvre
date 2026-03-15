package com.example.esgdp.csvreapi.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CustomerCardDto {
    // 從外部 API 取得
    private String custCode;
    private String custShortname;
    private String custRegion;

    // 從 DB 取得
    private List<ThirdPartyDto> toThirdParties;
    private List<ThirdPartyDto> fromThirdParties;
    private List<Integer> participateOptions;
    private Integer participateSinceYear;
    private Integer calenderSinceMonth;
    private Integer calenderToMonth;

    // rePreference: region -> bundleType -> List<RePreferenceItemDto>
    private Map<String, Map<String, List<RePreferenceItemDto>>> rePreference;

    private PlanDto expected;
    private PlanDto commitment;
}
