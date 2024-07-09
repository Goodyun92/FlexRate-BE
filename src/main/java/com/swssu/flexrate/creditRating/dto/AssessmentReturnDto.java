package com.swssu.flexrate.creditRating.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AssessmentReturnDto {
    private Float interestRate;     // 금리
    private Long loanLimit;     // 대출 한도
}
