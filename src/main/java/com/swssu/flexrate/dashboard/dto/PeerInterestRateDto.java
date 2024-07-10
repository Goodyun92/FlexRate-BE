package com.swssu.flexrate.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeerInterestRateDto {
    @JsonProperty("min_rate")
    private Float peerMinRate;

    @JsonProperty("max_rate")
    private Float peerMaxRate;

    @JsonProperty("user_rate")
    private Float peerUserRate;
}
