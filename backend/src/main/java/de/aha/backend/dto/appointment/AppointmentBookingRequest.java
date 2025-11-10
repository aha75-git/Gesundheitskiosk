package de.aha.backend.dto.appointment;

import de.aha.backend.model.appointment.AppointmentType;
import de.aha.backend.model.appointment.Priority;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AppointmentBookingRequest {
    @NotNull
    private String advisorId;

    @NotNull
    private AppointmentType type;

    @NotNull
    private LocalDateTime scheduledAt;

    @NotNull
    private Integer duration;

    private String notes;

    @NotEmpty
    private List<String> symptoms;

    @NotNull
    private Priority priority;
}
