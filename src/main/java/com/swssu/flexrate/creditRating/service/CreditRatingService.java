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
import com.swssu.flexrate.dashboard.domain.PeerEmploymentType;
import com.swssu.flexrate.dashboard.domain.PeerIncomeType;
import com.swssu.flexrate.dashboard.domain.PeerInterestRate;
import com.swssu.flexrate.dashboard.domain.SimilarIncomeInterestRate;
import com.swssu.flexrate.dashboard.dto.*;
import com.swssu.flexrate.dashboard.repository.PeerEmploymentTypeRepository;
import com.swssu.flexrate.dashboard.repository.PeerIncomeTypeRepository;
import com.swssu.flexrate.dashboard.repository.PeerInterestRateRepository;
import com.swssu.flexrate.dashboard.repository.SimilarIncomeInterestRateRepository;
import com.swssu.flexrate.exception.AppException;
import com.swssu.flexrate.exception.enums.ErrorCode;
import com.swssu.flexrate.loan.domain.Loan;
import com.swssu.flexrate.loan.repository.LoanRepository;
import com.swssu.flexrate.user.domain.Customer;
import com.swssu.flexrate.user.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.format.DateTimeFormatter;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class CreditRatingService {

    private final CustomerRepository customerRepository;
    private final CreditRatingInfoRepository creditRatingInfoRepository;
    private final LoanRepository loanRepository;
    private final PeerEmploymentTypeRepository peerEmploymentTypeRepository;
    private final PeerIncomeTypeRepository peerIncomeTypeRepository;
    private final PeerInterestRateRepository peerInterestRateRepository;
    private final SimilarIncomeInterestRateRepository similarIncomeInterestRateRepository;

    private final WebClient webClient;

    @Value("${model.base-url}")
    private String baseUrl;

    @Transactional
    public Mono<AssessmentReturnDto> assess(AssessmentRequestDto requestDto, String username){
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND, "사용자" + username + "이 없습니다."));

        //Boolean에서 Integer로 변환
        Integer gender = ( customer.getGender() ? 1 : 0 );
        log.info("gender:{}",gender);

        // LocalDate를 Integer로 변환
        Integer companyEnterMonth = Integer.parseInt(requestDto.getCompanyEnterMonth().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        log.info("companyEnterMonth:{}",companyEnterMonth);

        PredictLoanApiRequestDto apiRequestDto = PredictLoanApiRequestDto.builder()
                .employmentType(requestDto.getEmploymentType())
                .incomeType(requestDto.getIncomeType())
                .companyEnterMonth(companyEnterMonth)
                .yearlyIncome(requestDto.getYearlyIncome())
                .creditScore(requestDto.getCreditScore())
                .houseType(requestDto.getHouseType())
                .loanPurpose(requestDto.getLoanPurpose())
                .existingLoanCnt(requestDto.getExistingLoanCnt())
                .existingLoanAmt(requestDto.getExistingLoanAmt())
                .BirthYear(customer.getBirth())
                .gender(gender)
                .build();

        log.info("apiRequestDto:{}",apiRequestDto);

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

                    CreditRatingInfo creditRatingInfo;
                    Optional<CreditRatingInfo> optCreditRatingInfo = creditRatingInfoRepository.findByCustomer(customer);

                    if (optCreditRatingInfo.isPresent()){
                        CreditRatingInfo selectedCreditRatingInfo = optCreditRatingInfo.get();
                        selectedCreditRatingInfo.setEmploymentType(employmentType);
                        selectedCreditRatingInfo.setIncomeType(incomeType);
                        selectedCreditRatingInfo.setCompanyEnterMonth(requestDto.getCompanyEnterMonth());
                        selectedCreditRatingInfo.setYearlyIncome(requestDto.getYearlyIncome());
                        selectedCreditRatingInfo.setCreditScore(requestDto.getCreditScore());
                        selectedCreditRatingInfo.setHouseType(houseType);
                        selectedCreditRatingInfo.setLoanPurpose(loanPurpose);
                        selectedCreditRatingInfo.setExistingLoanCnt(requestDto.getExistingLoanCnt());
                        selectedCreditRatingInfo.setExistingLoanAmt(requestDto.getExistingLoanAmt());
                        creditRatingInfo = selectedCreditRatingInfo;

                    } else {
                        creditRatingInfo = CreditRatingInfo.builder()
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
                    }

                    creditRatingInfoRepository.save(creditRatingInfo);

                    modifyLoan(customer,creditRatingInfo);
                    updateDashboard(apiRequestDto,customer);

                    AssessmentReturnDto returnDto = AssessmentReturnDto.builder()
                            .interestRate(response.getInterestRate())
                            .loanLimit(response.getLoanLimit())
                            .build();
                    return Mono.just(returnDto);
                });
    }

    private void modifyLoan(Customer customer, CreditRatingInfo creditRatingInfo){
        //현재 진행중인 대출이 있다면 심사결과로 변동된 금리 대출에 적용

        if (!customer.getIsInProgress()){
            return;
        }

        loanRepository.findByCustomer(customer).ifPresent(loan -> {
            // 변동된 금리로 다시 계산

            Float interestRate = customer.getInterestRate();
            Float formattedinterestRate = interestRate / 100;
            Long principalAmount = loan.getPrincipalAmount();
            Float monthlyInterestRate = formattedinterestRate / 12;  //월 이자율
            Integer totalMonths = loan.getDuration() * 12;

            // 월 상환액 계산
            Double monthlyPaymentDouble = (principalAmount * monthlyInterestRate * Math.pow(1 + monthlyInterestRate, totalMonths))
                    / (Math.pow(1 + monthlyInterestRate, totalMonths) - 1);
            Long monthlyRepaymentAmount = Math.round(monthlyPaymentDouble);

            // 총 상환액 계산
            Long totalAmount = monthlyRepaymentAmount * totalMonths;

            // 총 이자액 계산
            Long interestAmount = totalAmount - principalAmount;

            loan.setTotalAmount(totalAmount);
            loan.setInterestAmount(interestAmount);
            loan.setMonthlyRepaymentAmount(monthlyRepaymentAmount);
            loan.setInterestRate(interestRate);

            loanRepository.save(loan);
        });

    }

    private void updateDashboard(PredictLoanApiRequestDto apiRequestDto, Customer customer){

        // 없는 경우 생성 (처음 조회)
        PeerEmploymentType peerEmploymentType = peerEmploymentTypeRepository.findByCustomer(customer)
                .orElseGet(() -> PeerEmploymentType.builder().customer(customer).build());

        PeerIncomeType peerIncomeType = peerIncomeTypeRepository.findByCustomer(customer)
                .orElseGet(() -> PeerIncomeType.builder().customer(customer).build());

        PeerInterestRate peerInterestRate = peerInterestRateRepository.findByCustomer(customer)
                .orElseGet(() -> PeerInterestRate.builder().customer(customer).build());

        SimilarIncomeInterestRate similarIncomeInterestRate = similarIncomeInterestRateRepository.findByCustomer(customer)
                .orElseGet(() -> SimilarIncomeInterestRate.builder().customer(customer).build());

        webClient.post()
                .uri(baseUrl+"/compare/loan")
                .body(BodyInserters.fromValue(apiRequestDto))
                .retrieve()
                .bodyToMono(DashboardReturnDto.class)
                .doOnSuccess(response -> {
                    // 성공 응답 처리

                    PeerEmploymentTypeDto peerEmploymentTypeDto = response.getPeerEmploymentTypeDto();
                    PeerIncomeTypeDto peerIncomeTypeDto = response.getPeerIncomeTypeDto();
                    PeerInterestRateDto peerInterestRateDto = response.getPeerInterestRateDto();
                    SimilarIncomeInterestRateDto similarIncomeInterestRateDto = response.getSimilarIncomeInterestRateDto();

                    peerEmploymentType.setEmploymentPermanent(peerEmploymentTypeDto.getEmploymentPermanent());
                    peerEmploymentType.setEmploymentContract(peerEmploymentTypeDto.getEmploymentContract());
                    peerEmploymentType.setEmploymentDaily(peerEmploymentTypeDto.getEmploymentDaily());
                    peerEmploymentType.setEmploymentEtc(peerEmploymentTypeDto.getEmploymentEtc());

                    peerIncomeType.setIncomeEmployeeO(peerIncomeTypeDto.getIncomeEmployeeO());
                    peerIncomeType.setIncomeEmployeeX(peerIncomeTypeDto.getIncomeEmployeeX());
                    peerIncomeType.setIncomeBusiness(peerIncomeTypeDto.getIncomeBusiness());
                    peerIncomeType.setIncomeFree(peerIncomeTypeDto.getIncomeFree());
                    peerIncomeType.setIncomeEtc(peerIncomeTypeDto.getIncomeEtc());

                    peerInterestRate.setPeerMaxRate(peerInterestRateDto.getPeerMaxRate());
                    peerInterestRate.setPeerMinRate(peerInterestRateDto.getPeerMinRate());
                    peerInterestRate.setPeerUserRate(peerInterestRateDto.getPeerUserRate());

                    similarIncomeInterestRate.setIncomeMaxRate(similarIncomeInterestRateDto.getIncomeMaxRate());
                    similarIncomeInterestRate.setIncomeMinRate(similarIncomeInterestRateDto.getIncomeMinRate());
                    similarIncomeInterestRate.setIncomeUserRate(similarIncomeInterestRateDto.getIncomeUserRate());

                    peerEmploymentTypeRepository.save(peerEmploymentType);
                    peerIncomeTypeRepository.save(peerIncomeType);
                    peerInterestRateRepository.save(peerInterestRate);
                    similarIncomeInterestRateRepository.save(similarIncomeInterestRate);

                })
                .doOnError(error -> {
                    // 오류 발생 처리
                    // 에러 로그
                })
                .subscribe(); // 비동기 호출 수행
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
