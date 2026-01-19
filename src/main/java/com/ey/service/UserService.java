package com.ey.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ey.entity.User;
import com.ey.enums.UserStatus;
import com.ey.repository.UserRepository;

@Service
public class UserService {
	private final UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User save(User user) {
		return userRepository.save(user);
	}

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public boolean emailExists(String email) {
		return userRepository.existsByEmail(email);
	}

	public void deactivateUser(User user) {
		user.setStatus(UserStatus.INACTIVE);
		userRepository.save(user);
	}
}