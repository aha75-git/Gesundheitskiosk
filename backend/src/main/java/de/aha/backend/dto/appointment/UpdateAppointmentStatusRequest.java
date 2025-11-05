package de.aha.backend.dto.appointment;

import de.aha.backend.model.appointment.AppointmentStatus;
import lombok.Builder;

@Builder
public record UpdateAppointmentStatusRequest(AppointmentStatus status) {
}
