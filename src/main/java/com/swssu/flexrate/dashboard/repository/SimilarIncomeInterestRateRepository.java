package com.swssu.flexrate.dashboard.repository;

import com.swssu.flexrate.dashboard.domain.PeerInterestRate;
import com.swssu.flexrate.dashboard.domain.SimilarIncomeInterestRate;
import com.swssu.flexrate.user.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SimilarIncomeInterestRateRepository extends JpaRepository<SimilarIncomeInterestRate,Long> {
    Optional<SimilarIncomeInterestRate> findByCustomer(Customer customer);

}
