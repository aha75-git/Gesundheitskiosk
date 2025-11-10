package de.aha.backend.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EmergencyContact {
    private String name;
    private String phone;
    private String relationship;
}
