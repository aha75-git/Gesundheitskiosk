package de.aha.backend.model.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPreferences {
    private String language;
    private NotificationSettings notificationSettings;
}
