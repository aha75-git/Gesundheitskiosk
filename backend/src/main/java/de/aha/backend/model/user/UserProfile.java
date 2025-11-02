package de.aha.backend.model.user;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserProfile {
    private PersonalData personalData;
    private MedicalInfo medicalInfo;
    private ContactInfo contactInfo;
    private List<String> languages;
    private String specialization;
    private String bio;
    private String qualification;
    private Double rating;
    private Integer reviewCount;
}