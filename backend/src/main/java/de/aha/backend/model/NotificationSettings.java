package de.aha.backend.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationSettings {
    private boolean emailNotifications;
    private boolean smsNotifications;
}
