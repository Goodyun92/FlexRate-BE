package com.swssu.flexrate.dashboard.service;

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
import com.swssu.flexrate.user.domain.Customer;
import com.swssu.flexrate.user.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final CustomerRepository customerRepository;
    private final PeerEmploymentTypeRepository peerEmploymentTypeRepository;
    private final PeerIncomeTypeRepository peerIncomeTypeRepository;
    private final PeerInterestRateRepository peerInterestRateRepository;
    private final SimilarIncomeInterestRateRepository similarIncomeInterestRateRepository;

    public DashboardReturnDto getDashboard(String username){
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(()->new AppException(ErrorCode.CONFLICT, "사용자의 통계를 찾을수가 없습니다."));

        PeerEmploymentType peerEmploymentType = peerEmploymentTypeRepository.findByCustomer(customer)
                .orElseThrow(()->new AppException(ErrorCode.CONFLICT, "사용자의 통계를 찾을수가 없습니다."));

        PeerIncomeType peerIncomeType = peerIncomeTypeRepository.findByCustomer(customer)
                .orElseThrow(()->new AppException(ErrorCode.CONFLICT, "사용자의 통계를 찾을수가 없습니다."));

        PeerInterestRate peerInterestRate = peerInterestRateRepository.findByCustomer(customer)
                .orElseThrow(()->new AppException(ErrorCode.CONFLICT, "사용자의 통계를 찾을수가 없습니다."));

        SimilarIncomeInterestRate similarIncomeInterestRate = similarIncomeInterestRateRepository.findByCustomer(customer)
                .orElseThrow(()->new AppException(ErrorCode.CONFLICT, "사용자의 통계를 찾을수가 없습니다."));

        PeerEmploymentTypeDto peerEmploymentTypeDto = PeerEmploymentTypeDto.builder()
                .employmentPermanent(peerEmploymentType.getEmploymentPermanent())
                .employmentContract(peerEmploymentType.getEmploymentContract())
                .employmentDaily(peerEmploymentType.getEmploymentDaily())
                .employmentEtc(peerEmploymentType.getEmploymentEtc())
                .build();
        PeerIncomeTypeDto peerIncomeTypeDto = PeerIncomeTypeDto.builder()
                .incomeEmployeeO(peerIncomeType.getIncomeEmployeeO())
                .incomeEmployeeX(peerIncomeType.getIncomeEmployeeX())
                .incomeBusiness(peerIncomeType.getIncomeBusiness())
                .incomeFree(peerIncomeType.getIncomeFree())
                .incomeEtc(peerIncomeType.getIncomeEtc())
                .build();
        PeerInterestRateDto peerInterestRateDto = PeerInterestRateDto.builder()
                .peerMaxRate(peerInterestRate.getPeerMaxRate())
                .peerMinRate(peerInterestRate.getPeerMinRate())
                .peerUserRate(peerInterestRate.getPeerUserRate())
                .build();
        SimilarIncomeInterestRateDto similarIncomeInterestRateDto = SimilarIncomeInterestRateDto.builder()
                .incomeMaxRate(similarIncomeInterestRate.getIncomeMaxRate())
                .incomeMinRate(similarIncomeInterestRate.getIncomeMinRate())
                .incomeUserRate(similarIncomeInterestRate.getIncomeUserRate())
                .build();

        return DashboardReturnDto.builder()
                .peerEmploymentTypeDto(peerEmploymentTypeDto)
                .peerIncomeTypeDto(peerIncomeTypeDto)
                .peerInterestRateDto(peerInterestRateDto)
                .similarIncomeInterestRateDto(similarIncomeInterestRateDto)
                .build();

    }
}
