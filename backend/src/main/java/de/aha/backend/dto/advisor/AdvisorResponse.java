package de.aha.backend.dto.advisor;

import de.aha.backend.model.advisor.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvisorResponse {
    private String id;
    private String name;
    private String specialization;
    private Double rating;
    private List<String> languages;
    private String imageUrl;
    private String email;
    private String phone;
    private String bio;
    private List<String> qualifications;
    private Integer experience;
    private Double consultationFee;
    private Boolean available;
    private Integer reviewCount;
    private List<Review> recentReviews;
    private Integer totalAppointments;
    private Integer completedAppointments;
}