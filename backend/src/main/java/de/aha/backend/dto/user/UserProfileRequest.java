package de.aha.backend.dto.user;

import de.aha.backend.model.user.ContactInfo;
import de.aha.backend.model.user.MedicalInfo;
import de.aha.backend.model.user.PersonalData;
import lombok.Builder;
import lombok.Data;

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
    private List<String> languages;
    private String specialization;
    private String bio;
    private String qualification;
    private Double rating;
}
