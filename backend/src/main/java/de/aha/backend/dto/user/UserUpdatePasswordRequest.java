package de.aha.backend.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for updating user's password.
 * Contains new and old password fields with validation constraints.
 */
public record UserUpdatePasswordRequest(
        @NotBlank(message = "Password cannot be blank")
        @Size(min = 5, max = 30, message = "Password must be between 5 and 30 characters")
        String password,

        @NotBlank(message = "Old password cannot be blank")
        @Size(min = 5, max = 30, message = "Old password must be between 5 and 30 characters")
        String oldPassword
) {}
