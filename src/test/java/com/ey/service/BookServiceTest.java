package com.ey.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Optional;
import com.ey.dto.request.UpdateBookRequest;
import com.ey.entity.Book;
import com.ey.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class BookServiceTest {
	@Autowired
	private BookService bookService;
	@MockBean
	private BookRepository bookRepository;

	@Test
	void create_shouldSaveBook() {
		Book book = new Book();
		book.setTitle("Clean Code");
		when(bookRepository.save(any(Book.class))).thenReturn(book);
		Book result = bookService.create(book);
		assertNotNull(result);
		assertEquals("Clean Code", result.getTitle());
	}

	@Test
	void createBook_shouldSaveBook() {
		Book book = new Book();
		book.setIsbn("123");
		when(bookRepository.save(book)).thenReturn(book);
		Book result = bookService.createBook(book);
		assertNotNull(result);
	}

	@Test
	void getAll_shouldReturnBookList() {
		when(bookRepository.findAll()).thenReturn(List.of(new Book(), new Book()));
		List<Book> books = bookService.getAll();
		assertEquals(2, books.size());
	}

	@Test
	void getById_shouldReturnBookWhenExists() {
		Book book = new Book();
		book.setId(1L);
		when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
		Book result = bookService.getById(1L);
		assertNotNull(result);
		assertEquals(1L, result.getId());
	}

	@Test
	void getById_shouldThrowExceptionWhenNotFound() {
		when(bookRepository.findById(1L)).thenReturn(Optional.empty());
		RuntimeException ex = assertThrows(RuntimeException.class, () -> bookService.getById(1L));
		assertEquals("Book not found", ex.getMessage());
	}

	@Test
	void delete_shouldCallRepositoryDelete() {
		doNothing().when(bookRepository).deleteById(1L);
		bookService.delete(1L);
		verify(bookRepository).deleteById(1L);
	}

	@Test
	void existsByIsbn_shouldReturnTrue() {
		when(bookRepository.existsByIsbn("ISBN123")).thenReturn(true);
		boolean exists = bookService.existsByIsbn("ISBN123");
		assertTrue(exists);
	}

	@Test
	void updateBook_shouldUpdateFieldsAndSave() {
		Book book = new Book();
		book.setCategory("Old");
		book.setLanguage("EN");
		book.setPublishedYear(2000);
		UpdateBookRequest request = new UpdateBookRequest();
		request.setCategory("New");
		request.setLanguage("FR");
		request.setPublishedYear(2024);
		when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
		when(bookRepository.save(book)).thenReturn(book);
		Book updated = bookService.updateBook(1L, request);
		assertEquals("New", updated.getCategory());
		assertEquals("FR", updated.getLanguage());
		assertEquals(2024, updated.getPublishedYear());
	}
}