package com.swssu.flexrate.user.controller;

import com.swssu.flexrate.exception.AppException;
import com.swssu.flexrate.exception.enums.ErrorCode;
import com.swssu.flexrate.global.dto.ReturnDto;
import com.swssu.flexrate.jwt.JwtService;
import com.swssu.flexrate.security.CustomUserDetails;
import com.swssu.flexrate.user.domain.Customer;
import com.swssu.flexrate.user.domain.User;
import com.swssu.flexrate.user.dto.*;
import com.swssu.flexrate.user.repository.UserRepository;
import com.swssu.flexrate.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/join")
    public ReturnDto<Void> joinCustomer(@RequestBody UserJoinRequestDto dto){
        userService.joinCustomer(dto);
        return ReturnDto.ok();
    }

    @PostMapping("/join/bank")
    public ReturnDto<Void> joinBank(@RequestBody BankJoinRequestDto dto){
        userService.joinBank(dto);
        return ReturnDto.ok();
    }

    @PostMapping("/login")
    @ResponseBody
    public ReturnDto<UserTokenReturnDto> login(@RequestBody UserLoginRequestDto dto, HttpServletResponse response){
        String accessToken = userService.login(dto.getUsername(), dto.getPassword());

        // Refresh Token 생성
        String refreshToken = jwtService.getRefreshToken(dto.getUsername());

        Cookie cookie = new Cookie("refreshToken", refreshToken);

        // 만료 7일
        cookie.setMaxAge(7 * 24 * 60 * 60);

        // 쿠키 옵션
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        // response
        response.addCookie(cookie);

        UserTokenReturnDto returnDto = UserTokenReturnDto.builder().token(accessToken).build();

        return ReturnDto.ok(returnDto);
    }

    @PostMapping("/refresh")
    public ReturnDto<UserTokenReturnDto> refresh (HttpServletRequest request) {

        // 쿠키에서 Refresh Token 추출
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        //username 추출
        String username = jwtService.getUsernameByRefreshToken(refreshToken);
        String newAccessToken = null;

        log.info("refreshToken:{}",refreshToken);
        log.info("userName:{}",username);

        if (jwtService.validateRefreshToken(refreshToken)) {
            // 새로운 Access Token 발급
            newAccessToken = jwtService.getAccessToken(username);// 새로운 Access Token 생성 로직
        }

        UserTokenReturnDto returnDto = UserTokenReturnDto.builder().token(newAccessToken).build();

        return ReturnDto.ok(returnDto);

    }

    @PatchMapping("/nickname")
    public ReturnDto<Void> updateNickName(@RequestBody UserNicknameRequestDto dto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("update start");
        // 현재 username
        String username = customUserDetails.getUser().getUsername();
        log.info("userName:{}",username);

        User user = userService.updateNickname(username, dto.getNickname());

        return ReturnDto.ok();
    }

    @GetMapping("/info")
    public ReturnDto<UserInfoReturnDto> info(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        //username 추출
        String username = customUserDetails.getUser().getUsername();

        UserInfoReturnDto dto = userService.getInfo(username);

        return ReturnDto.ok(dto);
    }



}
