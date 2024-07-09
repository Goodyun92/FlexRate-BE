package com.swssu.flexrate.creditRating.controller;

import com.swssu.flexrate.creditRating.dto.AssessmentRequestDto;
import com.swssu.flexrate.creditRating.dto.AssessmentReturnDto;
import com.swssu.flexrate.creditRating.dto.CreditRatingInfoReturnDto;
import com.swssu.flexrate.creditRating.service.CreditRatingService;
import com.swssu.flexrate.global.dto.ReturnDto;
import com.swssu.flexrate.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/credit-ratings")
public class CreditRatingController {

    private final CreditRatingService creditRatingService;

    @PostMapping("/assess")
    public Mono<ReturnDto<AssessmentReturnDto>> assess(@RequestBody AssessmentRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails customUserDetails){

        String username = customUserDetails.getUser().getUsername();

        return creditRatingService.assess(requestDto,username)
                .map(ReturnDto::ok);
    }

    @GetMapping("/info")
    public ReturnDto<CreditRatingInfoReturnDto> creditRatingInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails){

        String username = customUserDetails.getUser().getUsername();

        CreditRatingInfoReturnDto returnDto = creditRatingService.getInfo(username);

        return ReturnDto.ok(returnDto);

    }

}
