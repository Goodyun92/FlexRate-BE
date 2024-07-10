package com.swssu.flexrate.dashboard.repository;

import com.swssu.flexrate.dashboard.domain.PeerIncomeType;
import com.swssu.flexrate.dashboard.domain.PeerInterestRate;
import com.swssu.flexrate.user.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeerInterestRateRepository extends JpaRepository<PeerInterestRate,Long> {
    Optional<PeerInterestRate> findByCustomer(Customer customer);

}
