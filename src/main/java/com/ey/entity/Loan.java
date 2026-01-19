package com.ey.entity;

import java.time.LocalDateTime;

import com.ey.enums.LoanStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "loans")
public class Loan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "copy_id", nullable = false)
	private BookCopy bookCopy;
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	@Column(name = "issued_at")
	private LocalDateTime issuedAt;
	@Column(name = "due_at")
	private LocalDateTime dueAt;
	@Column(name = "returned_at")
	private LocalDateTime returnedAt;
	@Enumerated(EnumType.STRING)
	@Column(name="status")
	private LoanStatus status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BookCopy getBookCopy() {
		return bookCopy;
	}

	public void setBookCopy(BookCopy bookCopy) {
		this.bookCopy = bookCopy;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getIssuedAt() {
		return issuedAt;
	}

	public void setIssuedAt(LocalDateTime issuedAt) {
		this.issuedAt = issuedAt;
	}

	public LocalDateTime getDueAt() {
		return dueAt;
	}

	public void setDueAt(LocalDateTime dueAt) {
		this.dueAt = dueAt;
	}

	public LocalDateTime getReturnedAt() {
		return returnedAt;
	}

	public void setReturnedAt(LocalDateTime returnedAt) {
		this.returnedAt = returnedAt;
	}

	public LoanStatus getStatus() {
		return status;
	}

	public void setStatus(LoanStatus status) {
		this.status = status;
	}

}
