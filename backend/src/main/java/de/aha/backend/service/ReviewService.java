package de.aha.backend.service;

import de.aha.backend.dto.advisor.ReviewRequest;
import de.aha.backend.exception.ExecutionConflictException;
import de.aha.backend.exception.NotFoundObjectException;
import de.aha.backend.model.advisor.Advisor;
import de.aha.backend.model.advisor.Review;
import de.aha.backend.model.user.User;
import de.aha.backend.repository.AdvisorRepository;
import de.aha.backend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AdvisorRepository advisorRepository;
    private final UserService userService;

    @Transactional
    public Review addReview(String advisorId, ReviewRequest request, String userId) {
        log.info("Adding review for advisor: {} with rating: {}", advisorId, request.getRating());
        User patient = userService.findById(userId);

        // Prüfen, ob Berater existiert
        Advisor advisor = advisorRepository.findById(advisorId)
                .orElseThrow(() -> new NotFoundObjectException("Advisor not found with id: " + advisorId));

        // Prüfen, ob Patient bereits eine Bewertung abgegeben hat
        if (reviewRepository.existsByAdvisorIdAndPatientId(advisorId, patient.getId())) {
            throw new ExecutionConflictException("You have already reviewed this advisor");
        }

        String patientName = patient.getProfile().getPersonalData().getFirstName() +  " " + patient.getProfile().getPersonalData().getLastName();
        // Bewertung erstellen
        Review review = Review.builder()
                .advisorId(advisorId)
                .patientId(patient.getId())
                .patientName(patientName)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        Review savedReview = reviewRepository.save(review);

        // Berater-Statistiken aktualisieren
        updateAdvisorStats(advisorId);

        log.info("Review added successfully with id: {}", savedReview.getId());
        return savedReview;
    }

    @Transactional
    public void updateAdvisorStats(String advisorId) {
        // Durchschnittliche Bewertung berechnen
        Double averageRating = reviewRepository.findAverageRatingByAdvisorId(advisorId);
        Long reviewCount = reviewRepository.countByAdvisorId(advisorId);

        // Aktuelle Bewertungen abrufen (letzte 5)
        List<Review> recentReviews = reviewRepository.findByAdvisorIdOrderByCreationDateDesc(advisorId)
                .stream()
                .limit(5)
                .collect(java.util.stream.Collectors.toList());

        // Berater aktualisieren
        Advisor advisor = advisorRepository.findById(advisorId)
                .orElseThrow(() -> new RuntimeException("Advisor not found"));

        advisor.setRating(averageRating != null ? averageRating : 0.0);
        advisor.setReviewCount(reviewCount.intValue());
        advisor.setRecentReviews(recentReviews);
        advisor.setLastActive(LocalDateTime.now());

        advisorRepository.save(advisor);
    }

    public List<Review> getAdvisorReviews(String advisorId) {
        return reviewRepository.findByAdvisorIdOrderByCreationDateDesc(advisorId);
    }

    public List<Review> getAdvisorReviewsByRating(String advisorId, Integer minRating) {
        return reviewRepository.findByAdvisorIdAndRatingGreaterThanEqual(advisorId, minRating);
    }
}