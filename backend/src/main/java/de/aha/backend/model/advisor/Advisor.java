package de.aha.backend.model.advisor;

import de.aha.backend.model.AbstractDocument;
import de.aha.backend.model.appointment.WorkingHours;
import de.aha.backend.model.user.Address;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private Double rating = 5.0;

    @Indexed
    private List<String> languages;

    private String imageUrl;
    private String email;
    private String phone;
    private Address address;

    @TextIndexed
    private String bio;

    @Builder.Default
    private List<String> qualifications = new ArrayList<>();
    @Builder.Default
    private List<WorkingHours> workingHours = new ArrayList<>();

    @Builder.Default
    private Integer experience = 0; // Jahre Erfahrung
    @Builder.Default
    private Double consultationFee = 0.0; // Honorar pro Stunde

    @Builder.Default
    private Boolean available = true;

    @Builder.Default
    private Integer reviewCount = 0;

    @Builder.Default
    private List<Review> recentReviews = new ArrayList<>();

    // Statistische Felder für schnelle Suchoperationen
    @Builder.Default
    private Integer totalAppointments = 0;

    @Builder.Default
    private Integer completedAppointments = 0;

    @Builder.Default
    private LocalDateTime lastActive = LocalDateTime.now();

    // Advisor Entity (erweitert) um Chat Properties
    private String image;

    @Builder.Default
    private Boolean online = false;

    private String responseTime;

    @Builder.Default
    private LocalDateTime lastSeen = LocalDateTime.now();
}