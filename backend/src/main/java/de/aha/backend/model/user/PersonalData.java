package de.aha.backend.model.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PersonalData {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
}
