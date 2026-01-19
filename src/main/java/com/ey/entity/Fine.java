package com.ey.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "fines")
public class Fine extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
   @ManyToOne
   @JoinColumn(name = "loan_id", nullable = false)
   private Loan loan;
   private Double amount;
   private boolean paid;
   private boolean waived;
   private LocalDateTime calculatedAt;
   private LocalDateTime paidAt;
   public Loan getLoan() {
	return loan;
   }
   public void setLoan(Loan loan) {
	this.loan = loan;
   }
   public Double getAmount() {
	return amount;
   }
   public void setAmount(Double amount) {
	this.amount = amount;
   }
   public boolean isPaid() {
	return paid;
   }
   public void setPaid(boolean paid) {
	this.paid = paid;
   }
   public boolean isWaived() {
	return waived;
   }
   public void setWaived(boolean waived) {
	this.waived = waived;
   }
   public LocalDateTime getCalculatedAt() {
	return calculatedAt;
   }
   public void setCalculatedAt(LocalDateTime calculatedAt) {
	this.calculatedAt = calculatedAt;
   }
   public LocalDateTime getPaidAt() {
	return paidAt;
   }
   public void setPaidAt(LocalDateTime paidAt) {
	this.paidAt = paidAt;
   }
   
}