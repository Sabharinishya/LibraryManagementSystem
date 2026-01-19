package com.ey.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.request.CopyRequest;
import com.ey.dto.response.ApiResponse;
import com.ey.entity.BookCopy;
import com.ey.enums.CopyStatus;
import com.ey.service.CopyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/copies")
public class CopyController {
	private final CopyService copyService;

	public CopyController(CopyService copyService) {
		this.copyService = copyService;
	}
	@PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
	@PostMapping("/book/{bookId}")
	public ResponseEntity<ApiResponse<BookCopy>> addCopy(@PathVariable Long bookId,
			@Valid @RequestBody CopyRequest request) {
		BookCopy copy = new BookCopy();
		copy.setBarcode(request.getBarcode());
		copy.setShelfLocation(request.getShelfLocation());
		copy.setStatus(request.getStatus());
		return ResponseEntity.ok(new ApiResponse<>(true, copyService.addCopy(bookId, copy)));
	}
	@PreAuthorize("hasAnyRole('READER','LIBRARIAN')")
	@GetMapping("/available/{bookId}")
	public ResponseEntity<ApiResponse<List<BookCopy>>> availableCopies(@PathVariable Long bookId) {
		return ResponseEntity.ok(new ApiResponse<>(true, copyService.getAvailableCopies(bookId)));
	}

	@GetMapping("/{copyId}")
	public ResponseEntity<ApiResponse<BookCopy>> getCopy(@PathVariable Long copyId) {
		return ResponseEntity.ok(new ApiResponse<>(true, copyService.getById(copyId)));
	}
	@PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
	@PatchMapping("/{copyId}/status")
	public ResponseEntity<ApiResponse<BookCopy>> updateStatus(@PathVariable Long copyId,
			@RequestParam CopyStatus status) {
		return ResponseEntity.ok(new ApiResponse<>(true, copyService.updateStatus(copyId, status)));
	}
	
}