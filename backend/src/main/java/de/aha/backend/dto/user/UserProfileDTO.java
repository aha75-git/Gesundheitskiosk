package de.aha.backend.dto.user;

import de.aha.backend.model.appointment.WorkingHours;
import de.aha.backend.model.user.ContactInfo;
import de.aha.backend.model.user.MedicalInfo;
import de.aha.backend.model.user.PersonalData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
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
    @Builder.Default
    private List<WorkingHours> workingHours = new ArrayList<>();
}
