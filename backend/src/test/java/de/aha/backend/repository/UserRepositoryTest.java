package de.aha.backend.repository;

import de.aha.backend.model.user.User;
import de.aha.backend.model.user.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("hashedPassword");
        testUser.setUsername("mustermann");
        testUser.setEnabled(true);
        testUser.setRole(UserRole.ADVISOR);

        userRepository.save(testUser);
    }

    @Test
    void findByEmailIgnoreCase_ShouldReturnUser() {
        Optional<User> result = userRepository.findByEmailIgnoreCase("test@example.com");

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
        assertEquals("hashedPassword", result.get().getPassword());
    }

    @Test
    void findByEmailIgnoreCase_WithDifferentCase_ShouldReturnUser() {
        Optional<User> result = userRepository.findByEmailIgnoreCase("TEST@EXAMPLE.COM");

        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    void findByEmailIgnoreCase_WithNonExistentEmail_ShouldReturnEmpty() {
        Optional<User> result = userRepository.findByEmailIgnoreCase("nonexistent@example.com");

        assertFalse(result.isPresent());
    }

    @Test
    void saveAndFindById_ShouldWork() {
        User newUser = new User();
        newUser.setEmail("new@example.com");
        newUser.setPassword("newPassword");

        User savedUser = userRepository.save(newUser);
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertNotNull(savedUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("new@example.com", foundUser.get().getEmail());
    }

    @Test
    void deleteById_ShouldRemoveUser() {
        String userId = testUser.getId();

        userRepository.deleteById(userId);
        boolean exists = userRepository.existsById(userId);

        assertFalse(exists);
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        List<User> all = userRepository.findAll();

        assertEquals(1, all.size());
    }

    @Test
    void count_ShouldReturnCorrectCount() {
        long count = userRepository.count();

        assertEquals(1, count);
    }
}