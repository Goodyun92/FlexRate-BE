package com.swssu.flexrate.dashboard.controller;

import com.swssu.flexrate.dashboard.dto.DashboardReturnDto;
import com.swssu.flexrate.dashboard.service.DashboardService;
import com.swssu.flexrate.global.dto.ReturnDto;
import com.swssu.flexrate.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ReturnDto<DashboardReturnDto> getDashboard(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        String username = customUserDetails.getUser().getUsername();

        DashboardReturnDto returnDto = dashboardService.getDashboard(username);

        return ReturnDto.ok(returnDto);
    }
}
