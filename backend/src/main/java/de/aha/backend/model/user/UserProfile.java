package de.aha.backend.model.user;

import de.aha.backend.model.appointment.WorkingHours;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class UserProfile {
    private PersonalData personalData;
    private MedicalInfo medicalInfo;
    private ContactInfo contactInfo;
    @Builder.Default
    private List<String> languages = new ArrayList<>();
    private String specialization;
    private String bio;
    private String qualification;
    private Double rating;
    private Integer reviewCount;
}