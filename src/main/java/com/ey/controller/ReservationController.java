package com.ey.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.response.ApiResponse;
import com.ey.entity.Reservation;
import com.ey.enums.ReservationStatus;
import com.ey.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
   private final ReservationService reservationService;
   public ReservationController(ReservationService reservationService) {
       this.reservationService = reservationService;
   }
	@PreAuthorize("hasRole('READER')")
   @PostMapping
   public ResponseEntity<ApiResponse<Reservation>> placeReservation(
           @RequestParam Long bookId) {
       Reservation reservation = reservationService.placeReservation(1L, bookId);
       return ResponseEntity.ok(new ApiResponse<>(true, reservation));
   }
	@PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
   @PatchMapping("/{reservationId}")
   public ResponseEntity<ApiResponse<Reservation>> updateReservationStatus(
           @PathVariable Long reservationId,
           @RequestParam ReservationStatus status) {
       return ResponseEntity.ok(
               new ApiResponse<>(true,
                       reservationService.updateStatus(reservationId, status))
       );
   }
   @GetMapping("/me")
   public ResponseEntity<ApiResponse<List<Reservation>>> myReservations() {
       return ResponseEntity.ok(
               new ApiResponse<>(true,
                       reservationService.getUserReservations(1L))
       );
   }
   @DeleteMapping("/{reservationId}")
   public ResponseEntity<ApiResponse<String>> cancelReservation(
           @PathVariable Long reservationId) {
       reservationService.cancelReservation(reservationId);
       return ResponseEntity.ok(
               new ApiResponse<>(true, "Reservation cancelled successfully")
       );
   }
}
