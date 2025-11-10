package de.aha.backend.dto.user;

import de.aha.backend.model.user.UserRole;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RegisterRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validRequest_ShouldPassValidation() {
        RegisterRequest request = new RegisterRequest("test", "test@example.com", "password123", UserRole.USER);

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
        assertEquals("test@example.com", request.getEmail());
        assertEquals("password123", request.getPassword());
        assertEquals("test", request.getUsername());
        assertEquals(UserRole.USER, request.getRole());
    }

    @ParameterizedTest
    @MethodSource("invalidRequestProvider")
    void invalidRequest_ShouldFailValidation(String username, String email, String password, String expectedErrorMessage) {
        RegisterRequest request = new RegisterRequest(username, email, password,  UserRole.USER);

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains(expectedErrorMessage)),
                "Expected error message '" + expectedErrorMessage + "' not found in violations: " +
                        violations.stream().map(ConstraintViolation::getMessage).toList());
    }

    static Stream<Arguments> invalidRequestProvider() {
        return Stream.of(
                Arguments.of("test", "", "password123", "Email is required"),
                Arguments.of("test", null, "password123", "Email is required"),
                Arguments.of("test", "invalid-email", "password123", "Valid email is required"),
                Arguments.of("test", "test@example.com", "", "Password is required"),
                Arguments.of("test", "test@example.com", null, "Password is required"),
                Arguments.of("test", "test@example.com", "1234", "Password must be between 5 and 30 characters"),
                Arguments.of("test", "test@example.com", "a".repeat(31), "Password must be between 5 and 30 characters"),
                Arguments.of("", "test@example.com", "password123", "Username is required"),
                Arguments.of(null, "test@example.com", "password123", "Username is required")
                );
    }

    @Test
    void equals_ShouldReturnTrueForSameContent() {
        RegisterRequest request1 = new RegisterRequest("test", "test@example.com", "password123", UserRole.USER);
        RegisterRequest request2 = new RegisterRequest("test", "test@example.com", "password123", UserRole.USER);

        assertEquals(request1, request2);
    }

    @Test
    void equals_ShouldReturnFalseForDifferentContent() {
        RegisterRequest request1 = new RegisterRequest("test", "test1@example.com", "password123", UserRole.USER);
        RegisterRequest request2 = new RegisterRequest("test", "test2@example.com", "password123", UserRole.USER);

        assertNotEquals(request1, request2);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        RegisterRequest request1 = new RegisterRequest("test", "test@example.com", "password123", UserRole.USER);
        RegisterRequest request2 = new RegisterRequest("test", "test@example.com", "password123", UserRole.USER);

        assertEquals(request1.hashCode(), request2.hashCode());
    }
}