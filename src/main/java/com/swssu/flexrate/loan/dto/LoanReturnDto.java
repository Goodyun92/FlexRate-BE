package com.swssu.flexrate.loan.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
public class LoanReturnDto {
    private Integer duration;

    private LocalDate applyDate;

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
