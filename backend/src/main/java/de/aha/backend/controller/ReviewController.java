package de.aha.backend.controller;

import de.aha.backend.dto.advisor.ReviewRequest;
import de.aha.backend.model.advisor.Review;
import de.aha.backend.security.AuthInterceptor;
import de.aha.backend.security.AuthRequired;
import de.aha.backend.service.ReviewService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@AuthRequired(AuthInterceptor.class)
@Tag(name = "advisor review", description = "advisor review endpoints")
public class ReviewController {

    private final ReviewService reviewService;
    private final AuthInterceptor authInterceptor;

    @PostMapping("/{advisorId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Review> addReview(
            @PathVariable String advisorId,
            @Valid @RequestBody ReviewRequest request) {
        Review review = reviewService.addReview(advisorId, request, authInterceptor.getUserId());
        return ResponseEntity.ok(review);
    }

    @GetMapping("/{advisorId}")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<Review>> getAdvisorReviews(@PathVariable String advisorId) {
        List<Review> reviews = reviewService.getAdvisorReviews(advisorId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{advisorId}/filter")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<Review>> getAdvisorReviewsByRating(
            @PathVariable String advisorId,
            @RequestParam Integer minRating) {
        List<Review> reviews = reviewService.getAdvisorReviewsByRating(advisorId, minRating);
        return ResponseEntity.ok(reviews);
    }
}