package de.aha.backend.dto.user;

import de.aha.backend.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for user response.
 * Contains user's email information.
 */
@Getter
@Setter
@Builder
public class UserResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private UserRole role;
    private String email;
}