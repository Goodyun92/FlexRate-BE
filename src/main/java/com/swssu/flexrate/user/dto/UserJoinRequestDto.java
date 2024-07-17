package com.swssu.flexrate.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class UserJoinRequestDto {
    private String username;
    private String password;
    private String nickname;
    private Integer birth;
    private Boolean gender;
}
