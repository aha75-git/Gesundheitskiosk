package de.aha.backend.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Medication {
    private String name;
    private String dosage;
    private String frequency;
}
