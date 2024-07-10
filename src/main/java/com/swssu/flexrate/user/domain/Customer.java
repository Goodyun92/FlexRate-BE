package com.swssu.flexrate.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.swssu.flexrate.creditRating.domain.CreditRatingInfo;
import com.swssu.flexrate.loan.domain.Loan;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {
    private Integer birth;

    private Boolean gender;     // true(1):남 fasle(0):여

    private Float interestRate;     // 금리

    private Long loanLimit;     // 대출 한도

    private Boolean isInProgress;   // 대출 진행 중 여부

    //양방향 연결 mappedBy 사용
    @JsonIgnoreProperties("customer")
    @OneToOne(mappedBy = "customer", fetch = FetchType.LAZY)
    private CreditRatingInfo creditRatingInfo;

    //양방향 연결 mappedBy 사용
    @JsonIgnoreProperties("customer")
    @OneToOne(mappedBy = "customer", fetch = FetchType.LAZY)
    private Loan loan;
}
