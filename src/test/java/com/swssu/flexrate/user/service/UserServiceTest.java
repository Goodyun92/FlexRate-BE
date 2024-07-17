package com.swssu.flexrate.user.service;

import com.swssu.flexrate.exception.AppException;
import com.swssu.flexrate.user.domain.Customer;
import com.swssu.flexrate.user.dto.UserJoinRequestDto;
import com.swssu.flexrate.user.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void joinCustomer() {
        //given
        UserJoinRequestDto userJoinRequestDto = UserJoinRequestDto.builder()
                .username("user1")
                .password("pw30")
                .nickname("spring")
                .gender(true)
                .birth(1990)
                .build();

        //when
        try {
            Customer customer = userService.joinCustomer(userJoinRequestDto);

            //then
            Optional<Customer> findCustomer = customerRepository.findByUsername(customer.getUsername());
            assertThat(customer.getUsername()).isEqualTo(findCustomer.get().getUsername());
        }catch (AppException e){
            System.out.println("아이디 중복 예외");
            assertThat(e.getErrorCode().toString()).isEqualTo("USERNAME_DUPLICATED");
        }


    }

    @Test
    void joinBank() {
    }

    @Test
    void login() {
    }

    @Test
    void updateNickname() {
    }

    @Test
    void getInfo() {
    }
}