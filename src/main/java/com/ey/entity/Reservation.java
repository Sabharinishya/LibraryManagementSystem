package com.ey.entity;

import java.time.LocalDateTime;

import com.ey.enums.ReservationStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservations")
public class Reservation extends BaseEntity {
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	@ManyToOne
	@JoinColumn(name = "book_id", nullable = false)
	private Book book;
	@Enumerated(EnumType.STRING)
	private ReservationStatus status;
	private LocalDateTime expiresAt;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public ReservationStatus getStatus() {
		return status;
	}

	public void setStatus(ReservationStatus status) {
		this.status = status;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}

}
