package de.aha.backend.model.appointment;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.CompoundIndex;

import java.time.LocalDate;
import java.util.List;

// TODO wird diese Klasse benötigt?

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "advisor_availabilities")
@CompoundIndex(name = "advisor_date_idx", def = "{'advisorId': 1, 'date': 1}", unique = true)
public class AdvisorAvailability {
    @Id
    private String id;

    private String advisorId;
    private LocalDate date;
    private List<TimeSlot> availableSlots;
    private WorkingHours workingHours;

    @Builder.Default
    private Boolean isWorkingDay = true;
}