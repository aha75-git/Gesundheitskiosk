package de.aha.backend.model.appointment;

import de.aha.backend.model.advisor.Advisor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkingHours {
    private DayOfWeek dayOfWeek;
    private String startTime; // Format: HH:mm
    private String endTime;   // Format: HH:mm
    private boolean available;
}
