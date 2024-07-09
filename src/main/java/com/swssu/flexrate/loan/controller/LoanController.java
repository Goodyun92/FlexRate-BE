package com.swssu.flexrate.loan.controller;

import com.swssu.flexrate.global.dto.ReturnDto;
import com.swssu.flexrate.loan.dto.LoanApplicationRequestDto;
import com.swssu.flexrate.loan.dto.LoanReturnDto;
import com.swssu.flexrate.loan.service.LoanService;
import com.swssu.flexrate.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/apply")
    public ReturnDto<LoanReturnDto> loanApply(@RequestBody LoanApplicationRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails){

        String username = customUserDetails.getUser().getUsername();

        LoanReturnDto returnDto = loanService.apply(requestDto,username);

        return ReturnDto.ok(returnDto);
    }

    @GetMapping("/current-loan")
    public ReturnDto<LoanReturnDto> currentLoan(@AuthenticationPrincipal CustomUserDetails customUserDetails){

        String username = customUserDetails.getUser().getUsername();

        LoanReturnDto returnDto = loanService.currentLoan(username);

        return ReturnDto.ok(returnDto);
    }
}
