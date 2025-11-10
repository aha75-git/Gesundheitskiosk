package de.aha.backend.mapper;

import de.aha.backend.dto.appointment.AppointmentResponse;
import de.aha.backend.model.appointment.Appointment;

public class AppointmentMapper {

    public static AppointmentResponse mapToAppointmentResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatientId())
                .advisorId(appointment.getAdvisorId())
                .type(appointment.getType())
                .status(appointment.getStatus())
                .scheduledAt(appointment.getScheduledAt())
                .durationMinutes(appointment.getDurationMinutes())
                .notes(appointment.getNotes())
                .symptoms(appointment.getSymptoms())
                .priority(appointment.getPriority())
                .build();
    }
}
