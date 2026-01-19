package com.ey.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ey.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	List<Review> findByBookId(Long bookId);
}