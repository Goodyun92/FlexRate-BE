package com.swssu.flexrate.user.repository;

import com.swssu.flexrate.user.domain.Bank;
import com.swssu.flexrate.user.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    Optional<Bank> findByUsername(String username);
}
