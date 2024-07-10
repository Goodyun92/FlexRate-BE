package com.swssu.flexrate.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swssu.flexrate.dashboard.domain.PeerEmploymentType;
import com.swssu.flexrate.dashboard.domain.PeerIncomeType;
import com.swssu.flexrate.dashboard.domain.PeerInterestRate;
import com.swssu.flexrate.dashboard.domain.SimilarIncomeInterestRate;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DashboardReturnDto {
    @JsonProperty("peer_comparison")
    private PeerInterestRateDto peerInterestRateDto;

    @JsonProperty("income_comparison")
    private SimilarIncomeInterestRateDto similarIncomeInterestRateDto;

    @JsonProperty("peer_employment_types")
    private PeerEmploymentTypeDto peerEmploymentTypeDto;

    @JsonProperty("peer_income_types")
    private PeerIncomeTypeDto peerIncomeTypeDto;
}
