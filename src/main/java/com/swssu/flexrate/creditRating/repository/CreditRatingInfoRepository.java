package com.swssu.flexrate.creditRating.repository;

import com.swssu.flexrate.creditRating.domain.CreditRatingInfo;
import com.swssu.flexrate.user.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditRatingInfoRepository extends JpaRepository<CreditRatingInfo,Long>{
    Optional<CreditRatingInfo> findByCustomer(Customer customer);
}