package com.ey.dto.response;

public class DashboardResponse {
	private long totalLoans;
	private long overdueLoans;
	private double finesCollected;

	public long getTotalLoans() {
		return totalLoans;
	}

	public void setTotalLoans(long totalLoans) {
		this.totalLoans = totalLoans;
	}

	public long getOverdueLoans() {
		return overdueLoans;
	}

	public void setOverdueLoans(long overdueLoans) {
		this.overdueLoans = overdueLoans;
	}

	public double getFinesCollected() {
		return finesCollected;
	}

	public void setFinesCollected(double finesCollected) {
		this.finesCollected = finesCollected;
	}

}
