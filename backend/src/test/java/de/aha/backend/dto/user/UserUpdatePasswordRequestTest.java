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

class UserUpdatePasswordRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validRequest_ShouldPassValidation() {
        UserUpdatePasswordRequest request = new UserUpdatePasswordRequest("newPassword123", "oldPassword123");

        Set<ConstraintViolation<UserUpdatePasswordRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
        assertEquals("newPassword123", request.password());
        assertEquals("oldPassword123", request.oldPassword());
    }

    @ParameterizedTest
    @MethodSource("invalidRequestProvider")
    void invalidRequest_ShouldFailValidation(String password, String oldPassword, String expectedErrorMessage) {
        UserUpdatePasswordRequest request = new UserUpdatePasswordRequest(password, oldPassword);

        Set<ConstraintViolation<UserUpdatePasswordRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains(expectedErrorMessage)),
                "Expected error message '" + expectedErrorMessage + "' not found in violations: " +
                        violations.stream().map(ConstraintViolation::getMessage).toList());
    }

    static Stream<Arguments> invalidRequestProvider() {
        return Stream.of(
                Arguments.of("", "oldPassword123", "Password cannot be blank"),
                Arguments.of(null, "oldPassword123", "Password cannot be blank"),
                Arguments.of("1234", "oldPassword123", "Password must be between 5 and 30 characters"),
                Arguments.of("a".repeat(31), "oldPassword123", "Password must be between 5 and 30 characters"),
                Arguments.of("newPassword123", "", "Old password cannot be blank"),
                Arguments.of("newPassword123", null, "Old password cannot be blank"),
                Arguments.of("newPassword123", "1234", "Old password must be between 5 and 30 characters"),
                Arguments.of("newPassword123", "a".repeat(31), "Old password must be between 5 and 30 characters")
        );
    }
}