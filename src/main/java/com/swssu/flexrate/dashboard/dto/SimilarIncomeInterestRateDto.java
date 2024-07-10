package com.swssu.flexrate.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimilarIncomeInterestRateDto {
    @JsonProperty("min_rate")
    private Float incomeMinRate;

    @JsonProperty("max_rate")
    private Float incomeMaxRate;

    @JsonProperty("user_rate")
    private Float incomeUserRate;
}
