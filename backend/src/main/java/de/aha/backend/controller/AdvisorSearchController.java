package de.aha.backend.controller;

import de.aha.backend.dto.advisor.AdvisorSearchRequest;
import de.aha.backend.dto.advisor.AdvisorSearchResponse;
import de.aha.backend.model.advisor.Advisor;
import de.aha.backend.security.AuthInterceptor;
import de.aha.backend.security.AuthRequired;
import de.aha.backend.service.AdvisorSearchService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/searchadvisors")
@RequiredArgsConstructor
@AuthRequired(AuthInterceptor.class)
@Tag(name = "search advisors", description = "search advisors endpoints")
public class AdvisorSearchController {

    private final AdvisorSearchService advisorSearchService;

    @GetMapping("/search")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<AdvisorSearchResponse> searchAdvisors(
            @ModelAttribute AdvisorSearchRequest request) {
        AdvisorSearchResponse response = advisorSearchService.searchAdvisors(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/specializations")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<String>> getAllSpecializations() {
        List<String> specializations = advisorSearchService.getAllSpecializations();
        return ResponseEntity.ok(specializations);
    }

    @GetMapping("/languages")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<String>> getAllLanguages() {
        List<String> languages = advisorSearchService.getAllLanguages();
        return ResponseEntity.ok(languages);
    }

    @GetMapping("/featured")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<Advisor>> getFeaturedAdvisors() {
        List<Advisor> featuredAdvisors = advisorSearchService.getFeaturedAdvisors();
        return ResponseEntity.ok(featuredAdvisors);
    }

    @GetMapping("/{advisorId}/similar")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<List<Advisor>> getSimilarAdvisors(
            @PathVariable String advisorId,
            @RequestParam String specialization) {
        List<Advisor> similarAdvisors = advisorSearchService.getSimilarAdvisors(advisorId, specialization);
        return ResponseEntity.ok(similarAdvisors);
    }
}