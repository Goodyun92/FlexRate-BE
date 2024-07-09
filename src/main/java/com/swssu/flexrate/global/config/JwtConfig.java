package com.swssu.flexrate.global.config;

import com.swssu.flexrate.jwt.JwtTokenProvider;
import com.swssu.flexrate.jwt.RefreshTokenProvider;
import com.swssu.flexrate.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Value("${jwt.token.secret}")
    private String accessKey;

    @Value("${jwt.token.refresh}")
    private String refreshKey;

    private long accessExpireTimeMs = 1000 * 60 * 60 * 24 * 30L;    // 30일

    private long refreshExpireTimeMs = 1000 * 60 * 60 * 24 * 30L;   // 30일

    @Bean
    @Qualifier("access")
    public JwtTokenProvider accessJwtTokenProvider() {
        JwtTokenProvider accessJwtTokenProvider = new JwtTokenProvider(customUserDetailsService,accessKey,accessExpireTimeMs);
        return accessJwtTokenProvider;
    }

    @Bean
    @Qualifier("refresh")
    public RefreshTokenProvider refreshJwtTokenProvider() {
        RefreshTokenProvider refreshJwtTokenProvider = new RefreshTokenProvider(customUserDetailsService,refreshKey,refreshExpireTimeMs);
        return refreshJwtTokenProvider;
    }
}
