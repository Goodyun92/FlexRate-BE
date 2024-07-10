package com.swssu.flexrate.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeerIncomeTypeDto {
    @JsonProperty("employed_with_insurance")
    private Float incomeEmployeeO;

    @JsonProperty("employed_without_insurance")
    private Float incomeEmployeeX;

    @JsonProperty("self_employed")
    private Float incomeBusiness;

    @JsonProperty("freelancer")
    private Float incomeFree;

    @JsonProperty("other_income")
    private Float incomeEtc;
}
