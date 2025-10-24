package de.aha.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

/**
 * Authentication exception for application-specific runtime errors.
 */
@Getter
public class AppAuthenticationException extends AuthenticationException {
    private final ErrorType errorType;
    private final HttpStatus status;

    /**
     * Constructs a new AppAuthenticationException with the message,
     * specified cause and default error type and status.
     * @param msg the message
     * @param cause the underlying cause of the exception
     */
    public AppAuthenticationException(String msg, Throwable cause) {
        this(ErrorType.AUTHORIZATION_ERROR, HttpStatus.UNAUTHORIZED, msg, cause);
    }

    /**
     * Constructs a new AppAuthenticationException with the message
     * and default error type and status.
     * @param msg the message
     */
    public AppAuthenticationException(String msg) {
        this(ErrorType.AUTHORIZATION_ERROR, HttpStatus.UNAUTHORIZED, msg, null);
    }

    /**
     * Constructs a new ExecutionConflictException with custom error type, status, message, and cause.
     * @param errorType type of error
     * @param status HTTP status
     * @param message error message
     * @param error underlying cause
     */
    public AppAuthenticationException(ErrorType errorType, HttpStatus status, String message, Throwable error) {
        super(message, error);
        this.errorType = errorType;
        this.status = status;
    }
}
