package com.swssu.flexrate.bank.controller;

import com.swssu.flexrate.bank.dto.BankLoanRequestDto;
import com.swssu.flexrate.bank.service.BankService;
import com.swssu.flexrate.global.dto.ReturnDto;
import com.swssu.flexrate.loan.domain.Loan;
import com.swssu.flexrate.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/banks")
public class BankController {

    private final BankService bankService;

    @GetMapping("/loan-list")
    public ReturnDto<List<Loan>> getLoansByBank(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String username = customUserDetails.getUser().getUsername();
        List<Loan> loanList = bankService.getLoansByBank(username);
        return ReturnDto.ok(loanList);
    }

    @PostMapping("/approve")
    public ReturnDto<Void> LoanApprove(@RequestBody BankLoanRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        String username = customUserDetails.getUser().getUsername();
        bankService.approve(requestDto,username);
        return ReturnDto.ok();
    }

    @PostMapping("/reject")
    public ReturnDto<Void> LoanReject(@RequestBody BankLoanRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        String username = customUserDetails.getUser().getUsername();
        bankService.reject(requestDto,username);
        return ReturnDto.ok();
    }
}
