package com.swssu.flexrate.bank.service;

import com.swssu.flexrate.bank.dto.BankLoanRequestDto;
import com.swssu.flexrate.exception.AppException;
import com.swssu.flexrate.exception.enums.ErrorCode;
import com.swssu.flexrate.loan.domain.Loan;
import com.swssu.flexrate.loan.repository.LoanRepository;
import com.swssu.flexrate.user.domain.Bank;
import com.swssu.flexrate.user.domain.Customer;
import com.swssu.flexrate.user.repository.BankRepository;
import com.swssu.flexrate.user.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankService {
    private final LoanRepository loanRepository;
    private final BankRepository  bankRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public List<Loan> getLoansByBank(String username) {
        Bank bank = bankRepository.findByUsername(username)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND, "해당 은행 " + username + "이 존재하지 않습니다."));

        Long bankId = bank.getId();
        return loanRepository.findLoansByBankOrderByApprovalAndApplyDate(bankId);
    }

    public void approve(BankLoanRequestDto requestDto, String username){
        Long id = requestDto.getId();

        Loan loan = loanRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.LOAN_NOT_FOUND, id+ " : 해당 대출 신청이 존재하지 않습니다."));

        Bank bank = bankRepository.findByUsername(username)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND, "해당 은행 " + username + "이 존재하지 않습니다."));

        if (!loan.getBank().equals(bank)){
            throw new AppException(ErrorCode.FORBIDDEN, "해당 대출에 대한 권한이 없습니다.");
        }

        LocalDate startDate = LocalDate.now();

        // 시작일자 , 승인여부 수정
        loan.setApproval(true);
        loan.setStartDate(startDate);

        loanRepository.save(loan);
    }

    @Transactional
    public void reject(BankLoanRequestDto requestDto, String username){
        Long id = requestDto.getId();

        Loan loan = loanRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.LOAN_NOT_FOUND, id+ " : 해당 대출 신청이 존재하지 않습니다."));

        Bank bank = bankRepository.findByUsername(username)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND, "해당 은행 " + username + "이 존재하지 않습니다."));

        if (!loan.getBank().equals(bank)){
            throw new AppException(ErrorCode.FORBIDDEN, "해당 대출에 대한 권한이 없습니다.");
        }

        log.info("loan:{}",loan.getCustomer().getUsername());

        // 사용자 대출 진행 여부 false로 변경
        Customer customer = customerRepository.findByUsername(loan.getCustomer().getUsername())
                .orElseThrow(()->new AppException(ErrorCode.CONFLICT, ""));
        customer.setIsInProgress(false);
        customerRepository.save(customer);

        // 승인여부 수정
        loan.setApproval(false);
        loanRepository.save(loan);
    }
}
