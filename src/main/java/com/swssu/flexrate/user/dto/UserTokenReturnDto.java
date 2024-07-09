package com.swssu.flexrate.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTokenReturnDto {
    private String token;
}
