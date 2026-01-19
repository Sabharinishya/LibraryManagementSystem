package com.ey.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ey.dto.request.UpdateBookRequest;
import com.ey.entity.Book;
import com.ey.repository.BookRepository;

@Service
public class BookService {
	private final BookRepository bookRepository;

	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	public Book create(Book book) {
		return bookRepository.save(book);
	}

	public List<Book> getAll() {
		return bookRepository.findAll();
	}

	public Book getById(Long id) {
		return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
	}

	public void delete(Long id) {
		bookRepository.deleteById(id);
	}

	public boolean existsByIsbn(String isbn) {
		return bookRepository.existsByIsbn(isbn);
	}

	public Book updateBook(Long bookId, UpdateBookRequest request) {
		Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
		if (request.getCategory() != null) {
			book.setCategory(request.getCategory());
		}
		if (request.getLanguage() != null) {
			book.setLanguage(request.getLanguage());
		}
		if (request.getPublishedYear() != null) {
			book.setPublishedYear(request.getPublishedYear());
		}
		return bookRepository.save(book);
	}

	public Book createBook(Book book) {
		return bookRepository.save(book);
	}
}