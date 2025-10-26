package de.aha.backend.dto.user;

import de.aha.backend.model.UserRole;
import lombok.*;

/**
 * DTO for user response.
 * Contains user's email information.
 */
@Data
@Builder
public class UserResponse {
    private String token;
    @Builder.Default
    private String type = "Bearer";
    private String username;
    private UserRole role;
    private String email;
    private String createdAt;
}