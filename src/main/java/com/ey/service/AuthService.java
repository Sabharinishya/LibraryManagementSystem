package com.ey.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ey.dto.request.RegisterRequest;
import com.ey.entity.User;
import com.ey.enums.Role;
import com.ey.enums.UserStatus;

@Service
public class AuthService {
	private final UserService userService;
	private final BCryptPasswordEncoder passwordEncoder;

	public AuthService(UserService userService) {
		this.userService = userService;
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	public User register(RegisterRequest request) {
		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(request.getRole() != null ? request.getRole() : Role.READER);
		user.setStatus(UserStatus.ACTIVE);
		return userService.save(user);
	}

	public boolean validatePassword(String rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}
}
