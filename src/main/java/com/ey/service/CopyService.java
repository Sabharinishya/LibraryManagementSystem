package com.ey.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ey.entity.Book;
import com.ey.entity.BookCopy;
import com.ey.enums.CopyStatus;
import com.ey.repository.BookCopyRepository;
import com.ey.repository.BookRepository;

@Service
public class CopyService {
	private final BookCopyRepository copyRepository;
	private final BookRepository bookRepository;

	public CopyService(BookCopyRepository copyRepository, BookRepository bookRepository) {
		this.copyRepository = copyRepository;
		this.bookRepository = bookRepository;
	}

	public BookCopy addCopy(Long bookId, BookCopy copy) {
		Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
		copy.setBook(book);
		copy.setStatus(copy.getStatus() != null ? copy.getStatus() : CopyStatus.AVAILABLE);
		return copyRepository.save(copy);
	}

	public List<BookCopy> getAvailableCopies(Long bookId) {
		return copyRepository.findByBookIdAndStatus(bookId, CopyStatus.AVAILABLE);
	}

	public BookCopy getById(Long copyId) {
		return copyRepository.findById(copyId).orElseThrow(() -> new RuntimeException("Copy not found"));
	}

	public BookCopy updateStatus(Long copyId, CopyStatus status) {
		BookCopy copy = getById(copyId);
		copy.setStatus(status);
		return copyRepository.save(copy);
	}
	
}
