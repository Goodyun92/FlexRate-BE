package com.swssu.flexrate.creditRating.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.swssu.flexrate.creditRating.enums.EmploymentType;
import com.swssu.flexrate.creditRating.enums.HouseType;
import com.swssu.flexrate.creditRating.enums.IncomeType;
import com.swssu.flexrate.creditRating.enums.LoanPurpose;
import com.swssu.flexrate.user.domain.Customer;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "creditRatingInfo")
public class CreditRatingInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnoreProperties("creditRatingInfo")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @Column(nullable = false)
    private EmploymentType employmentType;

    @Column(nullable = false)
    private IncomeType incomeType;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private LocalDate companyEnterMonth;

    @Column(nullable = false)
    private Long yearlyIncome;

    @Column(nullable = false)
    private Integer creditScore;

    @Column(nullable = false)
    private HouseType houseType;

    @Column(nullable = false)
    private LoanPurpose loanPurpose;

    @Column(nullable = false)
    private Integer existingLoanCnt;

    @Column(nullable = false)
    private Long existingLoanAmt;
}
