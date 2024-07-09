package com.swssu.flexrate.jwt;

import com.swssu.flexrate.security.CustomUserDetailsService;

public class RefreshTokenProvider extends JwtTokenProvider{
    public RefreshTokenProvider(CustomUserDetailsService customUserDetailsService, String secretKey, long tokenValidTime) {
        super(customUserDetailsService,secretKey,tokenValidTime);
    }
}
