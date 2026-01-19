package com.ey.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ey.entity.Fine;

public interface FineRepository extends JpaRepository<Fine, Long> {
	List<Fine> findByLoanUserId(Long userId);

	List<Fine> findByPaidFalseAndWaivedFalse();

	@Query("SELECT COALESCE(SUM(f.amount), 0) FROM Fine f WHERE f.paid = true")
	double sumByPaidTrue();
}