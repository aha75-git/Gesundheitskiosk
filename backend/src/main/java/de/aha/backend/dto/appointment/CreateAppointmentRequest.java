package de.aha.backend.dto.appointment;

import de.aha.backend.model.appointment.AppointmentType;
import de.aha.backend.model.appointment.Priority;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CreateAppointmentRequest(
        String advisorId,
        LocalDateTime scheduledAt,
        AppointmentType type,
        String notes,
        List<String> symptoms,
        Priority priority
) {}