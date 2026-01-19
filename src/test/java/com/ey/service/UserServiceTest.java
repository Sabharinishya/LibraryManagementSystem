package com.ey.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import com.ey.entity.User;
import com.ey.enums.Role;
import com.ey.enums.UserStatus;
import com.ey.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class UserServiceTest {
	@Autowired
	private UserService userService;
	@MockBean
	private UserRepository userRepository;

	@Test
	void save_shouldPersistUser() {
		User user = new User();
		user.setName("Nishya");
		user.setEmail("test@gmail.com");
		user.setRole(Role.READER);
		user.setStatus(UserStatus.ACTIVE);
		when(userRepository.save(user)).thenReturn(user);
		User saved = userService.save(user);
		assertNotNull(saved);
		assertEquals("test@gmail.com", saved.getEmail());
	}

	@Test
	void findByEmail_shouldReturnUser() {
		User user = new User();
		user.setEmail("test@gmail.com");
		when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));
		Optional<User> result = userService.findByEmail("test@gmail.com");
		assertTrue(result.isPresent());
		assertEquals("test@gmail.com", result.get().getEmail());
	}

	@Test
	void emailExists_shouldReturnTrueWhenEmailExists() {
		when(userRepository.existsByEmail("test@gmail.com")).thenReturn(true);
		boolean exists = userService.emailExists("test@gmail.com");
		assertTrue(exists);
	}

	@Test
	void deactivateUser_shouldSetStatusInactiveAndSave() {
		User user = new User();
		user.setStatus(UserStatus.ACTIVE);
		userService.deactivateUser(user);
		assertEquals(UserStatus.INACTIVE, user.getStatus());
		verify(userRepository).save(user);
	}
}