package com.ey.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.response.ApiResponse;
import com.ey.dto.response.DashboardResponse;
import com.ey.service.ReportService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
	private final ReportService reportService;

	public ReportController(ReportService reportService) {
		this.reportService = reportService;
	}
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/dashboard")
	public ResponseEntity<ApiResponse<DashboardResponse>> dashboard() {
		return ResponseEntity.ok(new ApiResponse<>(true, reportService.dashboard()));
	}
}