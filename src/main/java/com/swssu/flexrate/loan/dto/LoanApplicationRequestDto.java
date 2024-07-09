package com.swssu.flexrate.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoanApplicationRequestDto {
    private Integer duration;

    private Long principalAmount;
}
