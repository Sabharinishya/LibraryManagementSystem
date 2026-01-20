package com.ey.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ey.entity.Book;
import com.ey.entity.BookCopy;
import com.ey.entity.Fine;
import com.ey.entity.Loan;
import com.ey.entity.User;
import com.ey.enums.CopyStatus;
import com.ey.enums.LoanStatus;
import com.ey.enums.Role;
import com.ey.enums.UserStatus;
import com.ey.repository.BookCopyRepository;
import com.ey.repository.BookRepository;
import com.ey.repository.FineRepository;
import com.ey.repository.LoanRepository;
import com.ey.repository.UserRepository;

@SpringBootTest
@Transactional
class FineServiceTest {
	@Autowired
	private FineService fineService;
	@Autowired
	private FineRepository fineRepository;
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
		user.setName("Test User");
		user.setEmail("test@test.com");
		user.setPassword("password");
		user.setRole(Role.READER);
		user.setStatus(UserStatus.ACTIVE);
		return userRepository.save(user);
	}

	private BookCopy createBookCopy() {
		Book book = new Book();
		book.setTitle("Clean Code");
		book.setIsbn("ISBN-" + System.nanoTime());
		book.setAuthors("Robert Martin");
		book.setCategory("Programming");
		book.setLanguage("EN");
		book.setPublishedYear(2008);
		book = bookRepository.save(book);
		BookCopy copy = new BookCopy();
		copy.setBook(book);
		copy.setStatus(CopyStatus.AVAILABLE);
		return bookCopyRepository.save(copy);
	}

	private Loan createLoan(LocalDateTime dueAt, LocalDateTime returnedAt) {
		User user = createUser();
		BookCopy copy = createBookCopy();
		Loan loan = new Loan();
		loan.setUser(user);
		loan.setBookCopy(copy);
		loan.setIssuedAt(LocalDateTime.now().minusDays(10));
		loan.setDueAt(dueAt);
		loan.setReturnedAt(returnedAt);
		loan.setStatus(LoanStatus.RETURNED);
		return loanRepository.save(loan);
	}
	@Test
	void calculateFine_shouldCreateFineWhenOverdue() {
		Loan loan = createLoan(LocalDateTime.now().minusDays(5), LocalDateTime.now());
		Fine fine = fineService.calculateFine(loan.getId());
		assertNotNull(fine);
		assertTrue(fine.getAmount() > 0);
		assertFalse(fine.isPaid());
		assertFalse(fine.isWaived());
		assertNotNull(fine.getLoan());
		assertNotNull(fine.getLoan().getUser());
	}

	@Test
	void calculateFine_shouldReturnNullWhenNotOverdue() {
		Loan loan = createLoan(LocalDateTime.now().plusDays(2), LocalDateTime.now());
		Fine fine = fineService.calculateFine(loan.getId());
		assertNull(fine);
	}

	@Test
	void getUserFines_shouldReturnFineList() {
		Loan loan = createLoan(LocalDateTime.now().minusDays(4), LocalDateTime.now());
		fineService.calculateFine(loan.getId());
		List<Fine> fines = fineService.getUserFines(loan.getUser().getId());
		assertEquals(1, fines.size());
	}

}