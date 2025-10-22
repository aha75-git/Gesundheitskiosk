package de.aha.backend.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPreferences {
    private String language;
    private NotificationSettings notificationSettings;
}
