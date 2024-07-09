package com.swssu.flexrate.creditRating.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.swssu.flexrate.creditRating.enums.EmploymentType;
import com.swssu.flexrate.creditRating.enums.HouseType;
import com.swssu.flexrate.creditRating.enums.IncomeType;
import com.swssu.flexrate.creditRating.enums.LoanPurpose;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class AssessmentRequestDto {
    @JsonProperty("employment_type")
    private String employmentType;

    @JsonProperty("income_type")
    private String incomeType;

    @JsonProperty("company_enterMonth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private LocalDate companyEnterMonth;

    @JsonProperty("yearly_income")
    private Long yearlyIncome;

    @JsonProperty("credit_score")
    private Integer creditScore;

    @JsonProperty("house_type")
    private String houseType;

    @JsonProperty("loan_purpose")
    private String loanPurpose;

    @JsonProperty("existing_loan_cnt")
    private Integer existingLoanCnt;

    @JsonProperty("existing_loan_amt")
    private Long existingLoanAmt;
}
