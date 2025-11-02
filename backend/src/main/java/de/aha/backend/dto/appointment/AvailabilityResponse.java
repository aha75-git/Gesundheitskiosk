package de.aha.backend.dto.appointment;

import de.aha.backend.model.appointment.TimeSlot;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record AvailabilityResponse(
        String advisorId,
        LocalDate date,
        List<TimeSlot> availableSlots
) {}