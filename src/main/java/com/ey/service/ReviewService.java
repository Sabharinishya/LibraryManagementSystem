package com.ey.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ey.entity.Book;
import com.ey.entity.Review;
import com.ey.entity.User;
import com.ey.repository.BookRepository;
import com.ey.repository.ReviewRepository;
import com.ey.repository.UserRepository;

@Service
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final BookRepository bookRepository;
	private final UserRepository userRepository;

	public ReviewService(ReviewRepository reviewRepository, BookRepository bookRepository,
			UserRepository userRepository) {
		this.reviewRepository = reviewRepository;
		this.bookRepository = bookRepository;
		this.userRepository = userRepository;
	}

	public Review addReview(Long userId, Long bookId, Review review) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
		review.setUser(user);
		review.setBook(book);
		return reviewRepository.save(review);
	}

	public List<Review> getReviewsForBook(Long bookId) {
		return reviewRepository.findByBookId(bookId);
	}
}
