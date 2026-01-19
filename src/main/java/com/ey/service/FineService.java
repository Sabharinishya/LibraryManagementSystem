package com.ey.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ey.entity.Fine;
import com.ey.entity.Loan;
import com.ey.repository.FineRepository;
import com.ey.repository.LoanRepository;

@Service
public class FineService {
	private static final double FINE_PER_DAY = 10.0;
	private final FineRepository fineRepository;
	private final LoanRepository loanRepository;

	public FineService(FineRepository fineRepository, LoanRepository loanRepository) {
		this.fineRepository = fineRepository;
		this.loanRepository = loanRepository;
	}

	public Fine calculateFine(Long loanId) {
		Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new RuntimeException("Loan not found"));
		if (loan.getReturnedAt() == null || !loan.getReturnedAt().isAfter(loan.getDueAt())) {
			return null;
		}
		long overdueDays = ChronoUnit.DAYS.between(loan.getDueAt(), loan.getReturnedAt());
		Fine fine = new Fine();
		fine.setLoan(loan);
		fine.setAmount(overdueDays * FINE_PER_DAY);
		fine.setPaid(false);
		fine.setWaived(false);
		fine.setCalculatedAt(LocalDateTime.now());
		return fineRepository.save(fine);
	}

	public List<Fine> getUserFines(Long userId) {
		return fineRepository.findByLoanUserId(userId);
	}

	public Fine payFine(Long fineId) {
		Fine fine = fineRepository.findById(fineId).orElseThrow(() -> new RuntimeException("Fine not found"));
		fine.setPaid(true);
		fine.setPaidAt(LocalDateTime.now());
		return fineRepository.save(fine);
	}

	public List<Fine> unpaidFines() {
		return fineRepository.findByPaidFalseAndWaivedFalse();
	}

	public Fine waiveFine(Long fineId) {
		Fine fine = fineRepository.findById(fineId).orElseThrow(() -> new RuntimeException("Fine not found"));
		fine.setWaived(true);
		return fineRepository.save(fine);
	}
}
