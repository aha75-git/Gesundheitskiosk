package de.aha.backend.exception;

/**
 * Base unchecked exception for application-specific runtime errors.
 */
public class AppRuntimeException extends RuntimeException {

    /**
     * Constructs a new AppRuntimeException with the specified cause.
     * @param cause the underlying cause of the exception
     */
    public AppRuntimeException(Throwable cause) {
        super(cause);
    }
}