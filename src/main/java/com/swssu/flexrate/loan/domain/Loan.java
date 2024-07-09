package com.swssu.flexrate.loan.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swssu.flexrate.user.domain.Bank;
import com.swssu.flexrate.user.domain.Customer;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "loan")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private LocalDate applyDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private LocalDate startDate;

    @Column(nullable = false)
    private Boolean isCompleted;

    @Column(nullable = false)
    private Long totalAmount;

    @Column(nullable = false)
    private Long principalAmount;

    @Column(nullable = false)
    private Long interestAmount;

    @Column(nullable = false)
    private Long monthlyRepaymentAmount;

    @Column(nullable = false)
    private Float interestRate;

    private Boolean approval;   //승인결정 아직안함 == null

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "bank_id", referencedColumnName = "id")
    private Bank bank;
}
