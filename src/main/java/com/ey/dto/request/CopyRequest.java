package com.ey.dto.request;

import com.ey.enums.CopyStatus;

import jakarta.validation.constraints.NotBlank;

public class CopyRequest {
	@NotBlank
	private String barcode;
	private String shelfLocation;
	private CopyStatus status;

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getShelfLocation() {
		return shelfLocation;
	}

	public void setShelfLocation(String shelfLocation) {
		this.shelfLocation = shelfLocation;
	}

	public CopyStatus getStatus() {
		return status;
	}

	public void setStatus(CopyStatus status) {
		this.status = status;
	}
}
