package de.aha.backend.dto.appointment;

import de.aha.backend.model.appointment.AppointmentStatus;
import de.aha.backend.model.appointment.AppointmentType;
import de.aha.backend.model.appointment.Priority;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record AppointmentResponse(
        String id,
        String patientId,
        String advisorId,
        AppointmentType type,
        AppointmentStatus status,
        LocalDateTime scheduledAt,
        int durationMinutes,
        String notes,
        List<String> symptoms,
        Priority priority
) {}