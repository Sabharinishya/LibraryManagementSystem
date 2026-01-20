package com.ey.service;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import com.ey.entity.User;
import com.ey.enums.Role;
import com.ey.enums.UserStatus;
@SpringBootTest
@Transactional
class UserServiceTest {
   @Autowired
   private UserService userService;
  
   private User createUserEntity() {
       User user = new User();
       user.setName("Test User");
       user.setEmail("user" + System.nanoTime() + "@test.com");
       user.setPassword("password");
       user.setRole(Role.READER);
       user.setStatus(UserStatus.ACTIVE);
       return user;
   }
   @Test
   void save_shouldPersistUser() {
       User user = createUserEntity();
       User saved = userService.save(user);
       assertNotNull(saved.getId());
       assertEquals(UserStatus.ACTIVE, saved.getStatus());
   }
   @Test
   void findByEmail_shouldReturnUserWhenExists() {
       User saved = userService.save(createUserEntity());
       Optional<User> found =
               userService.findByEmail(saved.getEmail());
       assertTrue(found.isPresent());
       assertEquals(saved.getId(), found.get().getId());
   }
   @Test
   void findByEmail_shouldReturnEmptyWhenNotExists() {
       Optional<User> found =
               userService.findByEmail("unknown@test.com");
       assertTrue(found.isEmpty());
   }
   @Test
   void emailExists_shouldReturnTrueWhenExists() {
       User user = createUserEntity();
       userService.save(user);
       boolean exists =
               userService.emailExists(user.getEmail());
       assertTrue(exists);
   }
 
}