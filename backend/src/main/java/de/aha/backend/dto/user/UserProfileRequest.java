package de.aha.backend.dto.user;

import de.aha.backend.model.appointment.WorkingHours;
import de.aha.backend.model.user.ContactInfo;
import de.aha.backend.model.user.MedicalInfo;
import de.aha.backend.model.user.PersonalData;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for user profile request.
 * Contains user profile fields.
 */
@Data
@Builder
public class UserProfileRequest {
    private String username;
    private String email;
    private PersonalData personalData;
    private MedicalInfo medicalInfo;
    private ContactInfo contactInfo;
    @Builder.Default
    private List<String> languages = new ArrayList<>();
    private String specialization;
    private String bio;
    private String qualification;
    private Double rating;
    @Builder.Default
    private List<WorkingHours> workingHours = new ArrayList<>();
}
