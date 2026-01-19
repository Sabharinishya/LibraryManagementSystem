package com.ey.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ey.entity.BookCopy;
import com.ey.entity.Loan;
import com.ey.enums.CopyStatus;
import com.ey.repository.BookCopyRepository;
import com.ey.repository.LoanRepository;

@SpringBootTest
class LoanServiceTest {
	@Autowired
	private LoanService loanService;
	@MockBean
	private LoanRepository loanRepository;
	@MockBean
	private BookCopyRepository bookCopyRepository;

	@Test
	void issueBook_shouldCreateLoanSuccessfully() {
		BookCopy copy = new BookCopy();
		copy.setStatus(CopyStatus.AVAILABLE);
		when(bookCopyRepository.findById(1L)).thenReturn(Optional.of(copy));
		when(bookCopyRepository.save(any(BookCopy.class))).thenReturn(copy);
		when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));
		loanService.issueBook(1L, 1);
		verify(bookCopyRepository).save(copy);
		verify(loanRepository).save(any());

	}
}