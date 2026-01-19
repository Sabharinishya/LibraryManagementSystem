package com.ey.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.request.BookRequest;
import com.ey.dto.request.UpdateBookRequest;
import com.ey.dto.response.ApiResponse;
import com.ey.entity.Book;
import com.ey.service.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookController {
	private final BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN')")
	@PostMapping
	public ResponseEntity<ApiResponse<Book>> createBook(@Valid @RequestBody BookRequest request) {
		if (bookService.existsByIsbn(request.getIsbn())) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(false, "ISBN already exists"));
		}
		Book book = new Book();
		book.setTitle(request.getTitle());
		book.setAuthors(request.getAuthors());
		book.setIsbn(request.getIsbn());
		book.setCategory(request.getCategory());
		book.setLanguage(request.getLanguage());
		book.setPublishedYear(request.getPublishedYear());
		return ResponseEntity.ok(new ApiResponse<>(true, bookService.create(book)));
	}
	@PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN','READER')")
	@GetMapping
	public ResponseEntity<ApiResponse<List<Book>>> getAllBooks() {
		return ResponseEntity.ok(new ApiResponse<>(true, bookService.getAll()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<Book>> getBook(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, bookService.getById(id)));
	}
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<String>> deleteBook(@PathVariable Long id) {
		bookService.delete(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Book deleted successfully"));
	}

	@PreAuthorize("hasAnyRole('LIBRARIAN','ADMIN')")
	@PutMapping("/{bookId}")
	public ResponseEntity<?> updateBook(@PathVariable Long bookId, @RequestBody UpdateBookRequest request) {
		return ResponseEntity.ok(bookService.updateBook(bookId, request));
	}
}