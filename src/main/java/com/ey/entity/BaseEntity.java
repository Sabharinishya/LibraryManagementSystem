package com.ey.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
@MappedSuperclass
public abstract class BaseEntity {
	

	@Column
	private LocalDateTime createdAt = LocalDateTime.now();
	private LocalDateTime updatedAt;

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}


	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

}
