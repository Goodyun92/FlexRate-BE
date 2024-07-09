package com.swssu.flexrate.jwt;

import com.swssu.flexrate.exception.AppException;
import com.swssu.flexrate.exception.enums.ErrorCode;
import com.swssu.flexrate.user.domain.User;
import com.swssu.flexrate.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final UserRepository userRepository;
    private final JwtTokenProvider accessJwtTokenProvider;
    private final RefreshTokenProvider refreshJwtTokenProvider;

    public JwtService(
            UserRepository userRepository,
            @Qualifier("access")JwtTokenProvider accessJwtTokenProvider,
            @Qualifier("refresh")RefreshTokenProvider refreshJwtTokenProvider
    ) {
        this.userRepository = userRepository;
        this.accessJwtTokenProvider = accessJwtTokenProvider;
        this.refreshJwtTokenProvider = refreshJwtTokenProvider;
    }

    public String getAccessToken(String userName){

        User selectedUser = userRepository.findByUsername(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND, "사용자"+userName + "이 없습니다."));

        return accessJwtTokenProvider.createToken(selectedUser.getUsername(), selectedUser.getRole());
    }

    public String getRefreshToken(String userName){

        User selectedUser = userRepository.findByUsername(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND, userName + "이 없습니다."));

        return refreshJwtTokenProvider.createToken(selectedUser.getUsername(), selectedUser.getRole());
    }

    public boolean validateRefreshToken(String refreshToken) {
        return refreshJwtTokenProvider.validateToken(refreshToken);
    }

    public String getUsernameByRefreshToken(String refreshToken){
        return refreshJwtTokenProvider.getUsername(refreshToken);
    }

}