package de.aha.backend.dto.user;

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

class UserLoginRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validRequest_ShouldPassValidation() {
        UserLoginRequest request = new UserLoginRequest("test@example.com", "password123");

        Set<ConstraintViolation<UserLoginRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
        assertEquals("test@example.com", request.email());
        assertEquals("password123", request.password());
    }

    @ParameterizedTest
    @MethodSource("invalidRequestProvider")
    void invalidRequest_ShouldFailValidation(String email, String password, String expectedErrorMessage) {
        UserLoginRequest request = new UserLoginRequest(email, password);

        Set<ConstraintViolation<UserLoginRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains(expectedErrorMessage)),
                "Expected error message '" + expectedErrorMessage + "' not found in violations: " +
                        violations.stream().map(ConstraintViolation::getMessage).toList());
    }

    static Stream<Arguments> invalidRequestProvider() {
        return Stream.of(
                Arguments.of("", "password123", "Email cannot be blank"),
                Arguments.of(null, "password123", "Email cannot be blank"),
                Arguments.of("invalid-email", "password123", "Invalid email format"),
                Arguments.of("test@example.com", "", "Password cannot be blank"),
                Arguments.of("test@example.com", null, "Password cannot be blank"),
                Arguments.of("test@example.com", "1234", "Password must be between 5 and 30 characters"),
                Arguments.of("test@example.com", "a".repeat(31), "Password must be between 5 and 30 characters")
        );
    }

    @Test
    void equals_ShouldReturnTrueForSameContent() {
        UserLoginRequest request1 = new UserLoginRequest("test@example.com", "password123");
        UserLoginRequest request2 = new UserLoginRequest("test@example.com", "password123");

        assertEquals(request1, request2);
    }

    @Test
    void equals_ShouldReturnFalseForDifferentContent() {
        UserLoginRequest request1 = new UserLoginRequest("test1@example.com", "password123");
        UserLoginRequest request2 = new UserLoginRequest("test2@example.com", "password123");

        assertNotEquals(request1, request2);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        UserLoginRequest request1 = new UserLoginRequest("test@example.com", "password123");
        UserLoginRequest request2 = new UserLoginRequest("test@example.com", "password123");

        assertEquals(request1.hashCode(), request2.hashCode());
    }
}