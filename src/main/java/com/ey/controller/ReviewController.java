package com.ey.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.request.ReviewRequest;
import com.ey.dto.response.ApiResponse;
import com.ey.entity.Review;
import com.ey.service.ReviewService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class ReviewController {
	private final ReviewService reviewService;

	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}
	@PreAuthorize("hasRole('READER')")
	@PostMapping("/{bookId}/reviews")
	public ResponseEntity<ApiResponse<Review>> addReview(@PathVariable Long bookId,
			@Valid @RequestBody ReviewRequest request) {
		Review review = new Review();
		review.setRating(request.getRating());
		review.setComment(request.getComment());
		return ResponseEntity.ok(new ApiResponse<>(true, reviewService.addReview(1L, bookId, review)));
	}
	@PreAuthorize("hasAnyRole('ADMIN','LIBRARIAN','READER')")
	@GetMapping("/{bookId}/reviews")
	public ResponseEntity<ApiResponse<List<Review>>> getReviews(@PathVariable Long bookId) {
		return ResponseEntity.ok(new ApiResponse<>(true, reviewService.getReviewsForBook(bookId)));
	}
}