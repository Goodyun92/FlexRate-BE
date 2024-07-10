package com.swssu.flexrate.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeerEmploymentTypeDto {
    @JsonProperty("full_time")
    private Float employmentPermanent;

    @JsonProperty("contract")
    private Float employmentContract;

    @JsonProperty("daily")
    private Float employmentDaily;

    @JsonProperty("others")
    private Float employmentEtc;
}
