package de.aha.backend.dto.user;

import de.aha.backend.model.UserProfile;

/**
 * DTO for user profile response.
 * Contains user porfile information.
 */
public record UserProfileResponse(UserProfile userProfile) {
}
