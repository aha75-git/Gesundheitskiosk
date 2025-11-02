package de.aha.backend.model.appointment;

import de.aha.backend.model.AbstractDocument;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "appointments")
public class Appointment extends AbstractDocument {
    @Indexed
    @Field("patient_id")
    private String patientId;
    @Indexed
    @Field("advisor_id")
    private String advisorId;
    private AppointmentType type;
    private AppointmentStatus status;
    @Indexed
    @Field("scheduled_at")
    private LocalDateTime scheduledAt;
    @Field("duration_minutes")
    private Integer durationMinutes;
    private String notes;
    private List<String> symptoms;
    private Priority priority;

    public boolean isUpcoming() {
        return scheduledAt.isAfter(LocalDateTime.now()) &&
                (status == AppointmentStatus.SCHEDULED || status == AppointmentStatus.CONFIRMED);
    }
}