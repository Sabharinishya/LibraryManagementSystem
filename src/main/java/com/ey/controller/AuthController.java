package com.ey.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ey.dto.request.LoginRequest;
import com.ey.dto.request.RegisterRequest;
import com.ey.dto.response.ApiResponse;
import com.ey.dto.response.AuthResponse;
import com.ey.dto.response.UserResponse;
import com.ey.entity.User;
import com.ey.service.AuthService;
import com.ey.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;
	private final UserService userService;

	public AuthController(AuthService authService, UserService userService) {
		this.authService = authService;
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
		if (userService.emailExists(request.getEmail())) {
			return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Email already exists"));
		}
		User user = authService.register(request);
		UserResponse response = new UserResponse();
		response.setId(user.getId());
		response.setName(user.getName());
		response.setEmail(user.getEmail());
		response.setRole(user.getRole());
		response.setStatus(user.getStatus());
		response.setCreatedAt(user.getCreatedAt());
		return ResponseEntity.ok(new ApiResponse<>(true, response));
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
		Optional<User> userOpt = userService.findByEmail(request.getEmail());
		if (userOpt.isEmpty()) {
			return ResponseEntity.status(401).body(new ApiResponse<>(false, "Invalid email or password"));
		}
		User user = userOpt.get();
		boolean valid = authService.validatePassword(request.getPassword(), user.getPassword());
		if (!valid) {
			return ResponseEntity.status(401).body(new ApiResponse<>(false, "Invalid email or password"));
		}
		AuthResponse response = new AuthResponse("LOGIN_SUCCESS");
		return ResponseEntity.ok(new ApiResponse<>(true, response));
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout() {
		return ResponseEntity.ok().body(java.util.Map.of("success", true, "message",
				"Logged out successfully. Please remove token from client."));
	}
}