package de.aha.backend.model.advisor;

import de.aha.backend.model.AbstractDocument;
import de.aha.backend.model.appointment.WorkingHours;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "advisors")
public class Advisor extends AbstractDocument {
    @TextIndexed
    private String name;

    @Indexed(unique = true)
    @Field("user_id")
    private String userId;

    @Indexed
    @TextIndexed
    private String specialization;

    @Builder.Default
    private Double rating = 0.0;

    @Indexed
    private List<String> languages;

    private String imageUrl;
    private String email;
    private String phone;

    @TextIndexed
    private String bio;

    private List<String> qualifications;
    private List<WorkingHours> workingHours;

    private Integer experience; // Jahre Erfahrung
    private Double consultationFee; // Honorar pro Stunde

    @Builder.Default
    private Boolean available = true;

    @Builder.Default
    private Integer reviewCount = 0;

    private List<Review> recentReviews;

    // Statistische Felder für schnelle Suchoperationen
    @Builder.Default
    private Integer totalAppointments = 0;

    @Builder.Default
    private Integer completedAppointments = 0;

    @Builder.Default
    private LocalDateTime lastActive = LocalDateTime.now();
}