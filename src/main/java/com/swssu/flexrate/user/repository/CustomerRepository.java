package com.swssu.flexrate.user.repository;

import com.swssu.flexrate.loan.domain.Loan;
import com.swssu.flexrate.user.domain.Customer;
import com.swssu.flexrate.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUsername(String username);

}
