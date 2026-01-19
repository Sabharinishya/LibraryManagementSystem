package com.ey.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.entity.Loan;
import com.ey.enums.LoanStatus;

public interface LoanRepository extends JpaRepository<Loan, Long> {
	List<Loan> findByUserId(Long userId);

	List<Loan> findByStatus(LoanStatus status);
}