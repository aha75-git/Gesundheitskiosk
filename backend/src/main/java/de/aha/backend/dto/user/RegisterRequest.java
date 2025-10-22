package de.aha.backend.dto.user;

import de.aha.backend.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for register a new user.
 * Contains email, username and password fields with validation constraints.
 * The field role is per default UserRole.USER
 */
@Getter
@Setter
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private UserRole role = UserRole.USER;
}