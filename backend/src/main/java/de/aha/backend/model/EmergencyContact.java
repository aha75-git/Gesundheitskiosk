package de.aha.backend.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmergencyContact {
    private String name;
    private String phone;
    private String relationship;
}
