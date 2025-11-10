package de.aha.backend.model.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Consent {
    private String type;
    private boolean granted;
    private LocalDateTime grantedAt;
    private String version;
}
