package com.swssu.flexrate.loan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public class LoanReturnDto {
    private Long id;

    private Integer duration;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private LocalDate applyDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private LocalDate startDate;

    private Boolean isCompleted;

    private Long totalAmount;

    private Long principalAmount;

    private Long interestAmount;

    private Long monthlyRepaymentAmount;

    private Float interestRate;

    private Boolean approval;

    private String bankName;
}
