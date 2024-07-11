package com.swssu.flexrate.loan.service;

import com.swssu.flexrate.creditRating.domain.CreditRatingInfo;
import com.swssu.flexrate.creditRating.repository.CreditRatingInfoRepository;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanService {

    private final CustomerRepository customerRepository;
    private final LoanRepository loanRepository;
    private final BankRepository bankRepository;
    private final CreditRatingInfoRepository creditRatingInfoRepository;

    @Transactional
    public LoanReturnDto apply(LoanApplicationRequestDto requestDto, String username){

        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND, "사용자" + username + "이 없습니다."));

        CreditRatingInfo creditRatingInfo = creditRatingInfoRepository.findByCustomer(customer)
                .orElseThrow(()->new AppException(ErrorCode.CREDITRATINGINFO_NOT_FOUND, "사용자" + username + "의 대출 심사 결과가 없습니다"));

        if (customer.getIsInProgress()){
            throw new AppException(ErrorCode.ALREADY_INPROGESS, "사용자가 이미 대출 프로세스를 진행 중 입니다.");
        }

        log.info("loanLimit:{}",customer.getLoanLimit());
        log.info("principalAmount:{}",requestDto.getPrincipalAmount());

        if (customer.getLoanLimit() < requestDto.getPrincipalAmount()){
            throw new AppException(ErrorCode.LOAN_LIMIT_EXCEEDED, "대출한도보다 높은 금액을 요청하였습니다.");
        }

        // 대출 은행 임의로 고정
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

        Loan loan;

        Optional<Loan> optLoan = loanRepository.findByCustomer(customer);

        if (optLoan.isPresent()) {
            Loan selectedLoan = optLoan.get();
            selectedLoan.setDuration(requestDto.getDuration());
            selectedLoan.setApplyDate(applyDate);
            selectedLoan.setStartDate(null);
            selectedLoan.setIsCompleted(false);
            selectedLoan.setTotalAmount(totalAmount);
            selectedLoan.setPrincipalAmount(principalAmount);
            selectedLoan.setInterestAmount(interestAmount);
            selectedLoan.setMonthlyRepaymentAmount(monthlyRepaymentAmount);
            selectedLoan.setInterestRate(customer.getInterestRate());
            selectedLoan.setApproval(null);
            selectedLoan.setBank(bank);
            loan = selectedLoan;
        } else {
            // 첫 대출 신청이여서 없는 객체가 없는 경우
            loan = Loan.builder()
                    .duration(requestDto.getDuration())
                    .applyDate(applyDate)
                    .startDate(null)
                    .isCompleted(false)
                    .totalAmount(totalAmount)
                    .principalAmount(principalAmount)
                    .interestAmount(interestAmount)
                    .monthlyRepaymentAmount(monthlyRepaymentAmount)
                    .interestRate(customer.getInterestRate())
                    .approval(null)
                    .bank(bank)
                    .customer(customer)
                    .build();
        }

        loanRepository.save(loan);

        //대출진행중으로 표시
        customer.setIsInProgress(true);
        customerRepository.save(customer);

        LoanReturnDto returnDto = LoanReturnDto.builder()
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
