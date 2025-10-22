package de.aha.backend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for user login request.
 * Contains email and password fields with validation constraints.
 */
public record UserLoginRequest(
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email cannot be blank")
        String email,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 5, max = 30, message = "Password must be between 5 and 30 characters")
        String password
) {}