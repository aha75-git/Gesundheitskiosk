package de.aha.backend.model;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class MedicalInfo {
    private String bloodType;
    private Set<String> allergies;
    private Set<String> chronicConditions;
    private Set<Medication> currentMedications;
    private EmergencyContact emergencyContact;
}