package de.aha.backend.model.appointment;

import de.aha.backend.model.AbstractDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "advisors")
public class Advisor extends AbstractDocument {
    private String name;
    private String specialization;
    private Double rating;
    private List<String> languages;
    private String imageUrl;
    private String email;
    private String phone;
    private String bio;
    private List<String> qualifications;
    private List<WorkingHours> workingHours;

    // Standard-Arbeitszeiten für Verfügbarkeitsprüfung
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkingHours {
        private DayOfWeek dayOfWeek;
        private String startTime; // Format: "09:00"
        private String endTime;   // Format: "17:00"
        private boolean available;
    }

    public enum DayOfWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }
}