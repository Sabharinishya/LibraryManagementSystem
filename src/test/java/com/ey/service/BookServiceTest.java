package com.ey.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ey.entity.Book;
import com.ey.repository.BookRepository;

@SpringBootTest
@Transactional
class BookServiceTest {
	@Autowired
	private BookService bookService;
	@Autowired
	private BookRepository bookRepository;

	private Book createValidBook(String title, String isbn) {
		Book book = new Book();
		book.setTitle(title);
		book.setIsbn(isbn);
		book.setAuthors("Robert C. Martin");
		book.setCategory("Programming");
		book.setLanguage("EN");
		book.setPublishedYear(2008);
		return book;
	}

	@Test
	void createBook_shouldSaveBook() {
		Book book = createValidBook("Effective Java", "ISBN002");
		Book saved = bookService.createBook(book);
		assertNotNull(saved.getId());
		assertEquals("ISBN002", saved.getIsbn());
	}

	@Test
	void getById_shouldReturnBookWhenExists() {
		Book book = createValidBook("DDD", "ISBN201");
		Book saved = bookRepository.save(book);
		Book result = bookService.getById(saved.getId());
		assertNotNull(result);
		assertEquals(saved.getId(), result.getId());
	}

	@Test
	void delete_shouldRemoveBook() {
		Book book = createValidBook("Delete Me", "ISBN301");
		Book saved = bookRepository.save(book);
		bookService.delete(saved.getId());
		assertTrue(bookRepository.findById(saved.getId()).isEmpty());
	}

	@Test
	void existsByIsbn_shouldReturnTrue() {
		Book book = createValidBook("Spring Boot", "ISBN401");
		bookRepository.save(book);
		boolean exists = bookService.existsByIsbn("ISBN401");
		assertTrue(exists);
	}

}