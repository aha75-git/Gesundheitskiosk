package de.aha.backend.model.appointment;

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
    private String start; // Format: HH:mm
    private String end;   // Format: HH:mm
    private boolean available;
}
