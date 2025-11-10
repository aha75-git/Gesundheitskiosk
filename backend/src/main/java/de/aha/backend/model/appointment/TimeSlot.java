package de.aha.backend.model.appointment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot {
    private LocalDateTime start;
    private LocalDateTime end;

    @Builder.Default
    private Boolean available = true;

    private String bookedBy; // userId if booked
}