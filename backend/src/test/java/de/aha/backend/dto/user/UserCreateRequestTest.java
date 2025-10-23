package de.aha.backend.dto.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserCreateRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void validRequest_passes() {
        UserCreateRequest r = new UserCreateRequest("test@example.com", "password123");
        Set<ConstraintViolation<UserCreateRequest>> v = validator.validate(r);
        assertTrue(v.isEmpty());
    }

    @Test
    void invalidEmail_fails() {
        UserCreateRequest r = new UserCreateRequest("bad-email", "password123");
        Set<ConstraintViolation<UserCreateRequest>> v = validator.validate(r);
        assertFalse(v.isEmpty());
    }

    @Test
    void shortPassword_fails() {
        UserCreateRequest r = new UserCreateRequest("test@example.com", "123");
        Set<ConstraintViolation<UserCreateRequest>> v = validator.validate(r);
        assertFalse(v.isEmpty());
    }
}