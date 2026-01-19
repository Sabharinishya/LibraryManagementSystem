package com.ey.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.response.ApiResponse;
import com.ey.entity.Fine;
import com.ey.service.FineService;

@RestController
@RequestMapping("/api/fines")
public class FineController {
	private final FineService fineService;

	public FineController(FineService fineService) {
		this.fineService = fineService;
	}
	@PreAuthorize("hasRole('READER')")
	@GetMapping("/me")
	public ResponseEntity<ApiResponse<List<Fine>>> myFines() {
		return ResponseEntity.ok(new ApiResponse<>(true, fineService.getUserFines(1L)));
	}
	@PreAuthorize("hasAnyRole('READER','LIBRARIAN')")
	@PostMapping("/{fineId}/pay")
	public ResponseEntity<ApiResponse<Fine>> payFine(@PathVariable Long fineId) {
		return ResponseEntity.ok(new ApiResponse<>(true, fineService.payFine(fineId)));
	}

	@GetMapping("/unpaid")
	public ResponseEntity<ApiResponse<List<Fine>>> unpaidFines() {
		return ResponseEntity.ok(new ApiResponse<>(true, fineService.unpaidFines()));
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{fineId}/waive")
	public ResponseEntity<ApiResponse<Fine>> waiveFine(@PathVariable Long fineId) {
		return ResponseEntity.ok(new ApiResponse<>(true, fineService.waiveFine(fineId)));
	}
}