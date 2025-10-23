package de.aha.backend.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MedicalInfo {
    private String bloodType;
    private List<String> allergies;
    private List<String> chronicConditions;
    private List<Medication> currentMedications;
    private EmergencyContact emergencyContact;
}