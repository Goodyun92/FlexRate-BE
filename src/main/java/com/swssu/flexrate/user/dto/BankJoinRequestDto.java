package com.swssu.flexrate.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BankJoinRequestDto {
    private String username;
    private String password;
    private String nickname;
}
