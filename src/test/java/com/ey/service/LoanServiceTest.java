package com.ey.service;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import com.ey.entity.*;
import com.ey.enums.*;
import com.ey.repository.*;

@SpringBootTest
@Transactional
class LoanServiceTest {
	@Autowired
	private LoanService loanService;
	@Autowired
	private LoanRepository loanRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private BookCopyRepository bookCopyRepository;

	private User createUser() {
		User user = new User();
		user.setName("Loan User");
		user.setEmail("loanuser" + System.nanoTime() + "@test.com");
		user.setPassword("password");
		user.setRole(Role.READER);
		user.setStatus(UserStatus.ACTIVE);
		return userRepository.save(user);
	}

	private BookCopy createAvailableCopy() {
		Book book = new Book();
		book.setTitle("Spring Boot");
		book.setIsbn("ISBN-" + System.nanoTime());
		book.setAuthors("Author");
		book.setCategory("Tech");
		book.setLanguage("EN");
		book.setPublishedYear(2023);
		book = bookRepository.save(book);
		BookCopy copy = new BookCopy();
		copy.setBook(book);
		copy.setStatus(CopyStatus.AVAILABLE);
		return bookCopyRepository.save(copy);
	}

	@Test
	void issueLoan_shouldCreateLoan() {
		User user = createUser();
		BookCopy copy = createAvailableCopy();
		Loan loan = loanService.issueLoan(user.getId(), copy.getId(), 7);
		assertNotNull(loan);
		assertEquals(user.getId(), loan.getUser().getId());
		assertEquals(copy.getId(), loan.getBookCopy().getId());
		assertEquals(LoanStatus.ISSUED, loan.getStatus());
		assertNotNull(loan.getIssuedAt());
		assertNotNull(loan.getDueAt());
		BookCopy updatedCopy = bookCopyRepository.findById(copy.getId()).get();
		assertEquals(CopyStatus.ON_LOAN, updatedCopy.getStatus());
	}

	@Test
	void issueLoan_shouldThrowExceptionWhenCopyNotAvailable() {
		User user = createUser();
		BookCopy copy = createAvailableCopy();
		copy.setStatus(CopyStatus.ON_LOAN);
		bookCopyRepository.save(copy);
		RuntimeException ex = assertThrows(RuntimeException.class,
				() -> loanService.issueLoan(user.getId(), copy.getId(), 5));
		assertEquals("Copy not available", ex.getMessage());
	}

	@Test
	void returnLoan_shouldMarkLoanReturned() {
		User user = createUser();
		BookCopy copy = createAvailableCopy();
		Loan loan = loanService.issueLoan(user.getId(), copy.getId(), 7);
		Loan returned = loanService.returnLoan(loan.getId());
		assertEquals(LoanStatus.RETURNED, returned.getStatus());
		assertNotNull(returned.getReturnedAt());
		BookCopy updatedCopy = bookCopyRepository.findById(copy.getId()).get();
		assertEquals(CopyStatus.AVAILABLE, updatedCopy.getStatus());
	}

	@Test
	void getLoansByUser_shouldReturnLoans() {
		User user = createUser();
		BookCopy copy1 = createAvailableCopy();
		BookCopy copy2 = createAvailableCopy();
		loanService.issueLoan(user.getId(), copy1.getId(), 7);
		loanService.issueLoan(user.getId(), copy2.getId(), 7);
		List<Loan> loans = loanRepository.findByUserId(user.getId());
		assertEquals(2, loans.size());
	}

	@Test
	void extendLoan_shouldExtendDueDate() {
		User user = createUser();
		BookCopy copy = createAvailableCopy();
		Loan loan = loanService.issueLoan(user.getId(), copy.getId(), 5);
		LocalDateTime oldDue = loan.getDueAt();
		Loan extended = loanService.extendLoan(loan.getId(), 3);
		assertEquals(oldDue.plusDays(3), extended.getDueAt());
	}

	@Test
	void returnLoan_shouldThrowExceptionWhenLoanNotFound() {
		RuntimeException ex = assertThrows(RuntimeException.class, () -> loanService.returnLoan(999L));
		assertEquals("Loan not found", ex.getMessage());
	}
}