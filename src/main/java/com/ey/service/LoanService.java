package com.ey.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ey.entity.BookCopy;
import com.ey.entity.Loan;
import com.ey.entity.User;
import com.ey.enums.CopyStatus;
import com.ey.enums.LoanStatus;
import com.ey.exception.CopyNotAvailableException;
import com.ey.exception.CopyNotFoundException;
import com.ey.repository.BookCopyRepository;
import com.ey.repository.LoanRepository;
import com.ey.repository.UserRepository;

@Service
public class LoanService {
	private final LoanRepository loanRepository;
	private final BookCopyRepository copyRepository;
	private final UserRepository userRepository;

	public LoanService(LoanRepository loanRepository, BookCopyRepository copyRepository,
			UserRepository userRepository) {
		this.loanRepository = loanRepository;
		this.copyRepository = copyRepository;
		this.userRepository = userRepository;
	}

	public Loan issueLoan(Long userId, Long copyId, int days) {
		BookCopy copy = copyRepository.findById(copyId).orElseThrow(() -> new RuntimeException("Copy not found"));
		if (copy.getStatus() != CopyStatus.AVAILABLE) {
			throw new RuntimeException("Copy not available");
		}
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		Loan loan = new Loan();
		loan.setUser(user);
		loan.setBookCopy(copy);
		loan.setIssuedAt(LocalDateTime.now());
		loan.setDueAt(LocalDateTime.now().plusDays(days));
		loan.setStatus(LoanStatus.ISSUED);
		copy.setStatus(CopyStatus.ON_LOAN);
		copyRepository.save(copy);
		return loanRepository.save(loan);
	}

	public Loan returnLoan(Long loanId) {
		Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new RuntimeException("Loan not found"));
		loan.setReturnedAt(LocalDateTime.now());
		loan.setStatus(LoanStatus.RETURNED);
		BookCopy copy = loan.getBookCopy();
		copy.setStatus(CopyStatus.AVAILABLE);
		copyRepository.save(copy);
		return loanRepository.save(loan);
	}

	public List<Loan> getLoansByUser(Long userId) {
		return loanRepository.findByUserId(userId);
	}

	public Loan extendLoan(Long loanId, int extraDays) {
		Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new RuntimeException("Loan not found"));
		loan.setDueAt(loan.getDueAt().plusDays(extraDays));
		return loanRepository.save(loan);
	}

	public List<Loan> overdueLoans() {
		return loanRepository.findByStatus(LoanStatus.OVERDUE);
	}

	public void issueBook(Long copyId, int days) {
		BookCopy copy = copyRepository.findById(copyId).orElseThrow(() -> new CopyNotFoundException("Copy not found"));
		if (copy.getStatus() != CopyStatus.AVAILABLE) {
			throw new CopyNotAvailableException("Copy not available");
		}
		 Loan loan = new Loan();
	       loan.setBookCopy(copy);
	       loan.setIssuedAt(LocalDateTime.now());
	       loan.setDueAt(LocalDateTime.now().plusDays(days));
	       loan.setStatus(LoanStatus.ACTIVE);
	       User user = userRepository.findById(1L)
	               .orElseThrow(() -> new RuntimeException("User not found"));
	       loan.setUser(user);
	       loanRepository.save(loan);
	       copy.setStatus(CopyStatus.ISSUED);
	       copyRepository.save(copy);
	   }
	}
