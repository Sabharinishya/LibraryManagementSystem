package com.ey.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.ey.entity.BookCopy;
import com.ey.entity.Loan;
import com.ey.entity.User;
import com.ey.enums.CopyStatus;
import com.ey.enums.LoanStatus;
import com.ey.exception.CopyNotAvailableException;
import com.ey.repository.BookCopyRepository;
import com.ey.repository.LoanRepository;
import com.ey.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class LoanServiceTest {
	@Autowired
	private LoanService loanService;
	@MockBean
	private LoanRepository loanRepository;
	@MockBean
	private BookCopyRepository bookCopyRepository;
	@MockBean
	private UserRepository userRepository;

	@Test
	void issueLoan_shouldCreateLoanSuccessfully() {
		BookCopy copy = new BookCopy();
		copy.setStatus(CopyStatus.AVAILABLE);
		User user = new User();
		when(bookCopyRepository.findById(1L)).thenReturn(Optional.of(copy));
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));
		Loan loan = loanService.issueLoan(1L, 1L, 7);
		assertNotNull(loan);
		assertEquals(LoanStatus.ISSUED, loan.getStatus());
		verify(bookCopyRepository).save(copy);
	}

	@Test
	void issueLoan_shouldThrowException_whenCopyNotAvailable() {
		BookCopy copy = new BookCopy();
		copy.setStatus(CopyStatus.ON_LOAN);
		when(bookCopyRepository.findById(1L)).thenReturn(Optional.of(copy));
		assertThrows(RuntimeException.class, () -> loanService.issueLoan(1L, 1L, 7));
	}

	@Test
	void returnLoan_shouldUpdateLoanAndCopy() {
		BookCopy copy = new BookCopy();
		copy.setStatus(CopyStatus.ON_LOAN);
		Loan loan = new Loan();
		loan.setBookCopy(copy);
		loan.setStatus(LoanStatus.ISSUED);
		when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
		when(loanRepository.save(any(Loan.class))).thenReturn(loan);
		Loan returned = loanService.returnLoan(1L);
		assertEquals(LoanStatus.RETURNED, returned.getStatus());
		verify(bookCopyRepository).save(copy);
	}

	@Test
	void getLoansByUser_shouldReturnLoans() {
		when(loanRepository.findByUserId(1L)).thenReturn(List.of(new Loan(), new Loan()));
		List<Loan> loans = loanService.getLoansByUser(1L);
		assertEquals(2, loans.size());
	}

	@Test
	void extendLoan_shouldUpdateDueDate() {
		Loan loan = new Loan();
		loan.setDueAt(LocalDateTime.now());
		when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
		when(loanRepository.save(loan)).thenReturn(loan);
		Loan extended = loanService.extendLoan(1L, 5);
		assertNotNull(extended.getDueAt());
	}

	@Test
	void overdueLoans_shouldReturnOverdueLoans() {
		when(loanRepository.findByStatus(LoanStatus.OVERDUE)).thenReturn(List.of(new Loan()));
		List<Loan> loans = loanService.overdueLoans();
		assertEquals(1, loans.size());
	}

	@Test
	void issueBook_shouldSaveLoanAndUpdateCopy() {
		BookCopy copy = new BookCopy();
		copy.setStatus(CopyStatus.AVAILABLE);
		User user = new User();
		when(bookCopyRepository.findById(1L)).thenReturn(Optional.of(copy));
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(loanRepository.save(any(Loan.class))).thenReturn(new Loan());
		loanService.issueBook(1L, 7);
		verify(loanRepository).save(any(Loan.class));
		verify(bookCopyRepository).save(copy);
	}

	@Test
	void issueBook_shouldThrowCopyNotAvailableException() {
		BookCopy copy = new BookCopy();
		copy.setStatus(CopyStatus.ISSUED);
		when(bookCopyRepository.findById(1L)).thenReturn(Optional.of(copy));
		assertThrows(CopyNotAvailableException.class, () -> loanService.issueBook(1L, 7));
	}
}