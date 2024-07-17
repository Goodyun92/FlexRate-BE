package com.swssu.flexrate.user.service;

import com.swssu.flexrate.exception.AppException;
import com.swssu.flexrate.exception.enums.ErrorCode;
import com.swssu.flexrate.jwt.JwtService;
import com.swssu.flexrate.user.domain.Bank;
import com.swssu.flexrate.user.domain.Customer;
import com.swssu.flexrate.user.domain.User;
import com.swssu.flexrate.user.dto.BankJoinRequestDto;
import com.swssu.flexrate.user.dto.UserInfoReturnDto;
import com.swssu.flexrate.user.dto.UserJoinRequestDto;
import com.swssu.flexrate.user.enums.Role;
import com.swssu.flexrate.user.repository.BankRepository;
import com.swssu.flexrate.user.repository.CustomerRepository;
import com.swssu.flexrate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.swssu.flexrate.user.enums.Role.ROLE_BANK;
import static com.swssu.flexrate.user.enums.Role.ROLE_CUSTOMER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final BankRepository bankRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtService jwtService;

    @Transactional
    public Customer joinCustomer(UserJoinRequestDto dto){   //customer 사용자 회원가입

        String username = dto.getUsername();
        String password = dto.getPassword();
        String nickName = dto.getNickname();
        Integer birth = dto.getBirth();
        Boolean gender = dto.getGender();

        // 빈 문자열 " " 도 예외처리하기 위해 trim 사용
        if (password == null || password.trim().isEmpty()){
            throw new AppException(ErrorCode.PASSWORD_BAD_REQUEST, "회원 가입시 패스워드는 비워둘 수 없습니다.");
        }

        if (nickName == null || nickName.trim().isEmpty()){
            throw new AppException(ErrorCode.NICKNAME_BAD_REQUEST, "회원 가입시 닉네임은 비워둘 수 없습니다.");
        }

        // userName 중복 check
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                            throw new AppException(ErrorCode.USERNAME_DUPLICATED, username + "는 이미 존재합니다.");
                        }
                );

        // 유저 저장
        Customer customer = Customer.builder()
                .username(username)
                .password(encoder.encode(password))
                .nickname(nickName)
                .role(ROLE_CUSTOMER)
                .birth(birth)
                .gender(gender)
                .isInProgress(false)
                .build();

        customerRepository.save(customer);

        return customer;
    }

    @Transactional
    public void joinBank(BankJoinRequestDto dto){   //bank 사용자 회원가입

        String username = dto.getUsername();
        String password = dto.getPassword();
        String nickName = dto.getNickname();

        // 빈 문자열 " " 도 예외처리하기 위해 trim 사용
        if (password == null || password.trim().isEmpty()){
            throw new AppException(ErrorCode.PASSWORD_BAD_REQUEST, "회원 가입시 패스워드는 비워둘 수 없습니다.");
        }

        if (nickName == null || nickName.trim().isEmpty()){
            throw new AppException(ErrorCode.NICKNAME_BAD_REQUEST, "회원 가입시 닉네임은 비워둘 수 없습니다.");
        }

        // userName 중복 check
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                            throw new AppException(ErrorCode.USERNAME_DUPLICATED, username + "는 이미 존재합니다.");
                        }
                );

        // 유저 저장
        Bank bank = Bank.builder()
                .username(username)
                .password(encoder.encode(password))
                .nickname(nickName)
                .role(ROLE_BANK)
                .build();


        bankRepository.save(bank);

    }

    public String login(String username, String password){

        //username 없음
        User selectedUser = userRepository.findByUsername(username)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND, username + "이 없습니다."));

        //password 틀림
        if(!encoder.matches(password, selectedUser.getPassword())){
            throw new AppException(ErrorCode.INVALID_PASSWORD, "패스워드를 잘못 입력했습니다.");
        }

        //access token 발행
        return jwtService.getAccessToken(selectedUser.getUsername());
    }

    public User updateNickname(String username, String nickname) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, "사용자"+ username + "이 없습니다."));

        user.setNickname(nickname);
        userRepository.save(user);
        return user;
    }

    public UserInfoReturnDto getInfo(String username){
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND, "사용자" + username + "이 없습니다."));

        UserInfoReturnDto dto = UserInfoReturnDto.builder()
                .username(customer.getUsername())
                .nickname(customer.getNickname())
                .role(customer.getRole())
                .birth(customer.getBirth())
                .gender(customer.getGender())
                .interestRate(customer.getInterestRate())
                .loanLimit(customer.getLoanLimit())
                .isInProgress(customer.getIsInProgress())
                .build();
        return dto;
    }

}
