package com.swssu.flexrate.dashboard.repository;

import com.swssu.flexrate.creditRating.domain.CreditRatingInfo;
import com.swssu.flexrate.dashboard.domain.PeerEmploymentType;
import com.swssu.flexrate.user.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeerEmploymentTypeRepository extends JpaRepository<PeerEmploymentType,Long> {
    Optional<PeerEmploymentType> findByCustomer(Customer customer);
}
