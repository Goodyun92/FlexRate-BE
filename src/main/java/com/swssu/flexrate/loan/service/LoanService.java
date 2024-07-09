package com.swssu.flexrate.loan.service;

import com.swssu.flexrate.exception.AppException;
import com.swssu.flexrate.exception.enums.ErrorCode;
import com.swssu.flexrate.loan.domain.Loan;
import com.swssu.flexrate.loan.dto.LoanApplicationRequestDto;
import com.swssu.flexrate.loan.dto.LoanReturnDto;
import com.swssu.flexrate.loan.repository.LoanRepository;
import com.swssu.flexrate.user.domain.Bank;
import com.swssu.flexrate.user.domain.Customer;
import com.swssu.flexrate.user.repository.BankRepository;
import com.swssu.flexrate.user.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final CustomerRepository customerRepository;
    private final LoanRepository loanRepository;
    private final BankRepository bankRepository;

    @Transactional
    public LoanReturnDto apply(LoanApplicationRequestDto requestDto, String username){

        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND, "사용자" + username + "이 없습니다."));

        if (customer.getIsInProgress()){
            new AppException(ErrorCode.ALREADY_INPROGESS, "사용자가 이미 대출 프로세스를 진행 중 입니다.");
        }

        if (customer.getLoanLimit()<requestDto.getPrincipalAmount()){
            new AppException(ErrorCode.LOAN_LIMIT_EXCEEDED, "대출한도보다 높은 금액을 요청하였습니다.");
        }

        //1번 은행으로 임의로 고정
        Bank bank = bankRepository.findById(1L)
                .orElseThrow(()->new AppException(ErrorCode.BANK_NOT_FOUND, "대출 가능한 은행이 없습니다."));



        /**
         * 원리금균등상환 방식 대출 금액 계산
         *
         */

        Float formattedinterestRate = customer.getInterestRate()/100;
        Long principalAmount = requestDto.getPrincipalAmount();

        Float monthlyInterestRate = formattedinterestRate/12;  //월 이자율
        Integer totalMonths = requestDto.getDuration() * 12;

        // 월 상환액 계산
        Double monthlyPaymentDouble = (principalAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, totalMonths))
                / (Math.pow(1 + monthlyInterestRate, totalMonths) - 1);
        Long monthlyRepaymentAmount = Math.round(monthlyPaymentDouble);

        // 총 상환액 계산
        Long totalAmount = monthlyRepaymentAmount * totalMonths;

        // 총 이자액 계산
        Long interestAmount = totalAmount - principalAmount;



        LocalDate applyDate = LocalDate.now();

        Loan loan = Loan.builder()
                .duration(requestDto.getDuration())
                .applyDate(applyDate)
                .isCompleted(false)
                .totalAmount(totalAmount)
                .principalAmount(principalAmount)
                .interestAmount(interestAmount)
                .monthlyRepaymentAmount(monthlyRepaymentAmount)
                .interestRate(customer.getInterestRate())
                .approval(false)
                .bank(bank)
                .customer(customer)
                .build();

        loanRepository.save(loan);

        //대출진행중으로 표시
        customer.setIsInProgress(true);
        customerRepository.save(customer);

        LoanReturnDto returnDto = LoanReturnDto.builder()
                .id(loan.getId())
                .duration(loan.getDuration())
                .applyDate(loan.getApplyDate())
                .startDate(loan.getStartDate())
                .isCompleted(loan.getIsCompleted())
                .totalAmount(loan.getTotalAmount())
                .principalAmount(loan.getPrincipalAmount())
                .interestAmount(loan.getInterestAmount())
                .monthlyRepaymentAmount(loan.getMonthlyRepaymentAmount())
                .interestRate(loan.getInterestRate())
                .approval(loan.getApproval())
                .bankName(loan.getBank().getNickname())
                .build();

        return returnDto;
    }

    public LoanReturnDto currentLoan(String username){
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND, "사용자" + username + "이 없습니다."));

        Loan loan = loanRepository.findByCustomer(customer)
                .orElseThrow(()->new AppException(ErrorCode.LOAN_NOT_FOUND, "사용자가 진행중인 대출이 없습니다."));

        LoanReturnDto returnDto = LoanReturnDto.builder()
                .id(loan.getId())
                .duration(loan.getDuration())
                .applyDate(loan.getApplyDate())
                .startDate(loan.getStartDate())
                .isCompleted(loan.getIsCompleted())
                .totalAmount(loan.getTotalAmount())
                .principalAmount(loan.getPrincipalAmount())
                .interestAmount(loan.getInterestAmount())
                .monthlyRepaymentAmount(loan.getMonthlyRepaymentAmount())
                .interestRate(loan.getInterestRate())
                .approval(loan.getApproval())
                .bankName(loan.getBank().getNickname())
                .build();

        return returnDto;

    }
}
