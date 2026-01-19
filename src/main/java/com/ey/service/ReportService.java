package com.ey.service;

import org.springframework.stereotype.Service;

import com.ey.dto.response.DashboardResponse;
import com.ey.enums.LoanStatus;
import com.ey.repository.FineRepository;
import com.ey.repository.LoanRepository;

@Service
public class ReportService {
	private final LoanRepository loanRepository;
	private final FineRepository fineRepository;

	public ReportService(LoanRepository loanRepository, FineRepository fineRepository) {
		this.loanRepository = loanRepository;
		this.fineRepository = fineRepository;
	}

	public DashboardResponse dashboard() {
		DashboardResponse response = new DashboardResponse();
		response.setTotalLoans(loanRepository.count());
		response.setOverdueLoans(loanRepository.findByStatus(LoanStatus.OVERDUE).size());
		response.setFinesCollected(fineRepository.sumByPaidTrue());
		return response;
	}
}
