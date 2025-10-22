package de.aha.backend.model;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserProfile {
    private PersonalData personalData;
    private MedicalInfo medicalInfo;
    private ContactInfo contactInfo;
    private String specialization;
    private Set<String> languages;
    private String qualification;
    private String bio;
    private Double rating;
    private Integer reviewCount;
}