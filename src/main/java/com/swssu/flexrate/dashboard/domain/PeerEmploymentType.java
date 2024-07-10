package com.swssu.flexrate.dashboard.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.swssu.flexrate.user.domain.Customer;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "peerEmploymentType")
public class PeerEmploymentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    private Float employmentPermanent;

    private Float employmentContract;

    private Float employmentDaily;

    private Float employmentEtc;

}
