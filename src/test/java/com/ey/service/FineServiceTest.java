package com.ey.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.ey.entity.Fine;
import com.ey.entity.Loan;
import com.ey.repository.FineRepository;
import com.ey.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class FineServiceTest {
	@Autowired
	private FineService fineService;
	@MockBean
	private FineRepository fineRepository;
	@MockBean
	private LoanRepository loanRepository;

	@Test
	void calculateFine_shouldCreateFineWhenOverdue() {
		Loan loan = new Loan();
		loan.setDueAt(LocalDateTime.now().minusDays(5));
		loan.setReturnedAt(LocalDateTime.now());
		when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
		when(fineRepository.save(org.mockito.ArgumentMatchers.any(Fine.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));
		Fine fine = fineService.calculateFine(1L);
		assertNotNull(fine);
		assertTrue(fine.getAmount() > 0);
		assertFalse(fine.isPaid());
		assertFalse(fine.isWaived());
	}

	@Test
	void calculateFine_shouldReturnNullWhenNotOverdue() {
		Loan loan = new Loan();
		loan.setDueAt(LocalDateTime.now().plusDays(2));
		loan.setReturnedAt(LocalDateTime.now());
		when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
		Fine fine = fineService.calculateFine(1L);
		assertNull(fine);
	}

	@Test
	void calculateFine_shouldThrowExceptionWhenLoanNotFound() {
		when(loanRepository.findById(1L)).thenReturn(Optional.empty());
		assertThrows(RuntimeException.class, () -> fineService.calculateFine(1L));
	}

	@Test
	void getUserFines_shouldReturnFineList() {
		when(fineRepository.findByLoanUserId(1L)).thenReturn(List.of(new Fine(), new Fine()));
		List<Fine> fines = fineService.getUserFines(1L);
		assertEquals(2, fines.size());
	}

	@Test
	void payFine_shouldMarkFineAsPaid() {
		Fine fine = new Fine();
		fine.setPaid(false);
		when(fineRepository.findById(1L)).thenReturn(Optional.of(fine));
		when(fineRepository.save(fine)).thenReturn(fine);
		Fine paidFine = fineService.payFine(1L);
		assertTrue(paidFine.isPaid());
		assertNotNull(paidFine.getPaidAt());
	}

	@Test
	void payFine_shouldThrowExceptionWhenFineNotFound() {
		when(fineRepository.findById(1L)).thenReturn(Optional.empty());
		assertThrows(RuntimeException.class, () -> fineService.payFine(1L));
	}

	@Test
	void unpaidFines_shouldReturnUnpaidFines() {
		when(fineRepository.findByPaidFalseAndWaivedFalse()).thenReturn(List.of(new Fine()));
		List<Fine> fines = fineService.unpaidFines();
		assertEquals(1, fines.size());
	}

	@Test
	void waiveFine_shouldMarkFineAsWaived() {
		Fine fine = new Fine();
		fine.setWaived(false);
		when(fineRepository.findById(1L)).thenReturn(Optional.of(fine));
		when(fineRepository.save(fine)).thenReturn(fine);
		Fine waived = fineService.waiveFine(1L);
		assertTrue(waived.isWaived());
	}
}