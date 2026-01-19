package com.ey.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.response.ApiResponse;
import com.ey.entity.Loan;
import com.ey.service.LoanService;

@RestController
@RequestMapping("/api/loans")
public class LoanController {
	private final LoanService loanService;

	public LoanController(LoanService loanService) {
		this.loanService = loanService;
	}

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<List<Loan>>> myLoans() {
		return ResponseEntity.ok(new ApiResponse<>(true, loanService.getLoansByUser(1L)));
	}

	@PatchMapping("/{loanId}/extend")
	public ResponseEntity<ApiResponse<Loan>> extendLoan(@PathVariable Long loanId, @RequestParam int extraDays) {
		return ResponseEntity.ok(new ApiResponse<>(true, loanService.extendLoan(loanId, extraDays)));
	}

	@GetMapping("/overdue")
	public ResponseEntity<ApiResponse<List<Loan>>> overdueLoans() {
		return ResponseEntity.ok(new ApiResponse<>(true, loanService.overdueLoans()));
	}
	@PreAuthorize("hasRole('READER')")
	@PostMapping("/issue/{copyId}")
	public ResponseEntity<String> issueBook(@PathVariable Long copyId, @RequestParam(defaultValue="14") int days) {
		loanService.issueBook(copyId, days);
		return ResponseEntity.ok("Book issued successfully");
	}
	@PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN','READER')")
	@PostMapping("/return/{loanId}")
	public ResponseEntity<String> returnBook(@PathVariable Long loanId) {
		loanService.returnLoan(loanId);
		return ResponseEntity.ok("Book returned");
	}
}
