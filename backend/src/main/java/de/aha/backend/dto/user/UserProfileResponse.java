package de.aha.backend.dto.user;

import de.aha.backend.model.user.UserProfile;
import lombok.Builder;

/**
 * DTO for user profile response.
 * Contains user porfile information.
 */
@Builder
public record UserProfileResponse(UserProfile userProfile) {
}
