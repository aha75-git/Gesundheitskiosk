package de.aha.backend.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PersonalData {
    private String firstName;
    private String lastName;
    private LocalDateTime dateOfBirth;
    private String gender;
}
