package com.ey.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ey.entity.Book;
import com.ey.entity.Reservation;
import com.ey.entity.User;
import com.ey.enums.ReservationStatus;
import com.ey.repository.BookRepository;
import com.ey.repository.ReservationRepository;
import com.ey.repository.UserRepository;

@Service
public class ReservationService {
	private final ReservationRepository reservationRepository;
	private final BookRepository bookRepository;
	private final UserRepository userRepository;

	public ReservationService(ReservationRepository reservationRepository, BookRepository bookRepository,
			UserRepository userRepository) {
		this.reservationRepository = reservationRepository;
		this.bookRepository = bookRepository;
		this.userRepository = userRepository;
	}

	public Reservation placeReservation(Long userId, Long bookId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
		Reservation reservation = new Reservation();
		reservation.setUser(user);
		reservation.setBook(book);
		reservation.setStatus(ReservationStatus.PENDING);
		reservation.setExpiresAt(LocalDateTime.now().plusDays(3));
		return reservationRepository.save(reservation);
	}

	public Reservation updateStatus(Long reservationId, ReservationStatus status) {
		Reservation reservation = reservationRepository.findById(reservationId)
				.orElseThrow(() -> new RuntimeException("Reservation not found"));
		reservation.setStatus(status);
		return reservationRepository.save(reservation);
	}

	public List<Reservation> getUserReservations(Long userId) {
		return reservationRepository.findByUserId(userId);
	}

	public void cancelReservation(Long reservationId) {
		Reservation reservation = reservationRepository.findById(reservationId)
				.orElseThrow(() -> new RuntimeException("Reservation not found"));
		reservation.setStatus(ReservationStatus.CANCELLED);
		reservationRepository.save(reservation);
	}
}