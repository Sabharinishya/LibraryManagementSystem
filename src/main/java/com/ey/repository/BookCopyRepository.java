package com.ey.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.entity.BookCopy;
import com.ey.enums.CopyStatus;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {

	List<BookCopy> findByBookIdAndStatus(Long bookId, CopyStatus available);
	
}