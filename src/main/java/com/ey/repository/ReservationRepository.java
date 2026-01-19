package com.ey.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	List<Reservation> findByUserId(Long userId);
}