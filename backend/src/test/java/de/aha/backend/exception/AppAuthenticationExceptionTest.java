package de.aha.backend.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class AppAuthenticationExceptionTest {
    @Test
    void constructor_setsFields() {
        AppAuthenticationException ex = new AppAuthenticationException("Invalid credentials");
        assertEquals(ErrorType.AUTHORIZATION_ERROR, ex.getErrorType());
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
        assertEquals("Invalid credentials", ex.getMessage());
    }
    @Test
    void fullConstructor_setsAllFields() {
        Throwable t = new RuntimeException("err");
        AppAuthenticationException ex = new AppAuthenticationException(ErrorType.AUTHORIZATION_ERROR, HttpStatus.UNAUTHORIZED, "msg", t);
        assertEquals(ErrorType.AUTHORIZATION_ERROR, ex.getErrorType());
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatus());
        assertEquals("msg", ex.getMessage());
        assertEquals(t, ex.getCause());
    }
}