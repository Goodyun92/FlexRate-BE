package com.swssu.flexrate.creditRating.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AssessmentReturnDto {
    @JsonProperty("loan_rate")
    private Float interestRate;     // 금리

    @JsonProperty("loan_limit")
    private Long loanLimit;     // 대출 한도
}
