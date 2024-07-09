package com.swssu.flexrate.creditRating.service;

import com.swssu.flexrate.creditRating.domain.CreditRatingInfo;
import com.swssu.flexrate.creditRating.dto.AssessmentRequestDto;
import com.swssu.flexrate.creditRating.dto.AssessmentReturnDto;
import com.swssu.flexrate.creditRating.dto.CreditRatingInfoReturnDto;
import com.swssu.flexrate.creditRating.dto.PredictLoanApiRequestDto;
import com.swssu.flexrate.creditRating.enums.EmploymentType;
import com.swssu.flexrate.creditRating.enums.HouseType;
import com.swssu.flexrate.creditRating.enums.IncomeType;
import com.swssu.flexrate.creditRating.enums.LoanPurpose;
import com.swssu.flexrate.creditRating.repository.CreditRatingInfoRepository;
import com.swssu.flexrate.exception.AppException;
import com.swssu.flexrate.exception.enums.ErrorCode;
import com.swssu.flexrate.user.domain.Customer;
import com.swssu.flexrate.user.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class CreditRatingService {

    private final CustomerRepository customerRepository;
    private final CreditRatingInfoRepository creditRatingInfoRepository;
    private final WebClient webClient;

    @Value("${model.base-url}")
    private String baseUrl;

    @Transactional
    public Mono<AssessmentReturnDto> assess(AssessmentRequestDto requestDto, String username){
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND, "사용자" + username + "이 없습니다."));
        Integer gender = ( customer.getGender() ? 1 : 0 );  //Boolean에서 Integer로 변환

        PredictLoanApiRequestDto apiRequestDto = PredictLoanApiRequestDto.builder()
                .employmentType(requestDto.getEmploymentType())
                .incomeType(requestDto.getIncomeType())
                .companyEnterMonth(requestDto.getCompanyEnterMonth())
                .yearlyIncome(requestDto.getYearlyIncome())
                .creditScore(requestDto.getCreditScore())
                .houseType(requestDto.getHouseType())
                .loanPurpose(requestDto.getLoanPurpose())
                .existingLoanCnt(requestDto.getExistingLoanCnt())
                .existingLoanAmt(requestDto.getExistingLoanAmt())
                .BirthYear(customer.getBirth())
                .gender(gender)
                .build();

        //enum 타입으로 변환 및 예외처리
        EmploymentType employmentType = EmploymentType.fromValue(requestDto.getEmploymentType());
        IncomeType incomeType = IncomeType.fromValue(requestDto.getIncomeType());
        HouseType houseType = HouseType.fromValue(requestDto.getHouseType());
        LoanPurpose loanPurpose = LoanPurpose.fromValue(requestDto.getLoanPurpose());

        return webClient.post()
                .uri(baseUrl+"/predict/loan")
                .body(BodyInserters.fromValue(apiRequestDto))
                .retrieve()
                .bodyToMono(AssessmentReturnDto.class)
                .flatMap(response->{
                    customer.setInterestRate(response.getInterestRate());
                    customer.setLoanLimit(response.getLoanLimit());
                    customerRepository.save(customer);

                    CreditRatingInfo creditRatingInfo = CreditRatingInfo.builder()
                            .customer(customer)
                            .employmentType(employmentType)
                            .incomeType(incomeType)
                            .companyEnterMonth(requestDto.getCompanyEnterMonth())
                            .yearlyIncome(requestDto.getYearlyIncome())
                            .creditScore(requestDto.getCreditScore())
                            .houseType(houseType)
                            .loanPurpose(loanPurpose)
                            .existingLoanCnt(requestDto.getExistingLoanCnt())
                            .existingLoanAmt(requestDto.getExistingLoanAmt())
                            .build();
                    creditRatingInfoRepository.save(creditRatingInfo);

                    AssessmentReturnDto returnDto = AssessmentReturnDto.builder()
                            .interestRate(response.getInterestRate())
                            .loanLimit(response.getLoanLimit())
                            .build();
                    return Mono.just(returnDto);
                });
    }

    public CreditRatingInfoReturnDto getInfo(String username){
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND, "사용자" + username + "이 없습니다."));

        CreditRatingInfo creditRatingInfo = creditRatingInfoRepository.findByCustomer(customer)
                .orElseThrow(()->new AppException(ErrorCode.CREDITRATINGINFO_NOT_FOUND, "사용자" + username + "의 신용평가정보가 없습니다."));

        CreditRatingInfoReturnDto dto = CreditRatingInfoReturnDto.builder()
                .employmentType(creditRatingInfo.getEmploymentType())
                .incomeType(creditRatingInfo.getIncomeType())
                .companyEnterMonth(creditRatingInfo.getCompanyEnterMonth())
                .yearlyIncome(creditRatingInfo.getYearlyIncome())
                .creditScore(creditRatingInfo.getCreditScore())
                .houseType(creditRatingInfo.getHouseType())
                .loanPurpose(creditRatingInfo.getLoanPurpose())
                .existingLoanCnt(creditRatingInfo.getExistingLoanCnt())
                .existingLoanAmt(creditRatingInfo.getExistingLoanAmt())
                .build();

        return dto;
    }
}
