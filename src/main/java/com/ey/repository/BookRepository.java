package com.ey.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
	boolean existsByIsbn(String isbn);

	List<Book> findByCategory(String category);

	List<Book> findByLanguage(String language);
}
