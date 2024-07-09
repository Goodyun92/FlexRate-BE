package com.swssu.flexrate.user.dto;

import com.swssu.flexrate.user.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoReturnDto {
    private String username;
    private String nickname;
    private Role role;
    private Integer birth;
    private Boolean gender;
    private Float interestRate;     // 금리
    private Long loanLimit;     // 대출 한도
    private Boolean isInProgress;   // 대출 진행 중 여부
}
