package com.swssu.flexrate.loan.repository;

import com.swssu.flexrate.loan.domain.Loan;
import com.swssu.flexrate.user.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface LoanRepository extends JpaRepository<Loan, Long>{
    Optional<Loan> findByCustomer(Customer customer);

    //JPQL
    @Query("SELECT l FROM Loan l " +
            "JOIN FETCH l.customer c " +
            "LEFT JOIN FETCH c.creditRatingInfo cri " +
            "WHERE l.bank.id = :bankId " +
            "ORDER BY l.approval NULLS FIRST, l.applyDate ASC")
    List<Loan> findLoansByBankOrderByApprovalAndApplyDate(@Param("bankId") Long bankId);
}
