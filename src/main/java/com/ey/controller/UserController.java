package com.ey.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.response.ApiResponse;
import com.ey.dto.response.UserResponse;
import com.ey.entity.User;
import com.ey.enums.Role;
import com.ey.enums.UserStatus;
import com.ey.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private final UserRepository userRepository;

	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<UserResponse>> getMyProfile() {
		User user = userRepository.findAll().stream().findFirst().orElse(null);
		if (user == null) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(false, "No user found"));
		}
		return ResponseEntity.ok(new ApiResponse<>(true, mapToResponse(user)));
	}
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
		List<UserResponse> users = userRepository.findAll().stream().map(this::mapToResponse)
				.collect(Collectors.toList());
		return ResponseEntity.ok(new ApiResponse<>(true, users));
	}
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{userId}/status")
	public ResponseEntity<ApiResponse<UserResponse>> updateUserStatus(@PathVariable Long userId,
			@RequestParam UserStatus status) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		user.setStatus(status);
		userRepository.save(user);
		return ResponseEntity.ok(new ApiResponse<>(true, mapToResponse(user)));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/{userId}/role")
	public ResponseEntity<ApiResponse<UserResponse>> updateUserRole(@PathVariable Long userId,
			@RequestParam Role role) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		user.setRole(role);
		userRepository.save(user);
		return ResponseEntity.ok(new ApiResponse<>(true, mapToResponse(user)));
	}

	private UserResponse mapToResponse(User user) {
		UserResponse response = new UserResponse();
		response.setId(user.getId());
		response.setName(user.getName());
		response.setEmail(user.getEmail());
		response.setRole(user.getRole());
		response.setStatus(user.getStatus());
		response.setCreatedAt(user.getCreatedAt());
		return response;
	}
}