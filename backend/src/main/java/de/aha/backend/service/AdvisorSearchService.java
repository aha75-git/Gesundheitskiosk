package de.aha.backend.service;

import de.aha.backend.dto.advisor.AdvisorSearchRequest;
import de.aha.backend.dto.advisor.AdvisorSearchResponse;
import de.aha.backend.model.advisor.Advisor;
import de.aha.backend.repository.AdvisorRepository;
import de.aha.backend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdvisorSearchService {

    private final AdvisorRepository advisorRepository;
    private final ReviewRepository reviewRepository;
    private final MongoTemplate mongoTemplate;

    public AdvisorSearchResponse searchAdvisors(AdvisorSearchRequest request) {
        log.info("Searching advisors with filters: {}", request);

        Pageable pageable = createPageable(request);
        Page<Advisor> advisorPage = executeSearch(request, pageable);

        System.out.println("searchAdvisors: " +  advisorPage.getContent());

        return buildSearchResponse(advisorPage, request);
    }

    private Pageable createPageable(AdvisorSearchRequest request) {
        Sort sort = createSort(request);
        return PageRequest.of(request.getPage(), request.getSize(), sort);
    }

    private Sort createSort(AdvisorSearchRequest request) {
        if (request.getSortBy() == null) {
            return Sort.by(Sort.Direction.DESC, "rating");
        }

        Sort.Direction direction = request.getSortDirection() != null
                ? request.getSortDirection()
                : Sort.Direction.DESC;

        switch (request.getSortBy().toLowerCase()) {
            case "rating":
                return Sort.by(direction, "rating");
            case "experience":
                return Sort.by(direction, "experience");
            case "fee":
                return Sort.by(direction, "consultationFee");
            case "name":
                return Sort.by(direction, "name");
            case "reviews":
                return Sort.by(direction, "reviewCount");
            default:
                return Sort.by(Sort.Direction.DESC, "rating");
        }
    }

    private Page<Advisor> executeSearch(AdvisorSearchRequest request, Pageable pageable) {
        // Wenn ein Suchbegriff vorhanden ist, verwende die Text-Suche
        if (request.getSearchQuery() != null && !request.getSearchQuery().trim().isEmpty()) {
            return advisorRepository.fullTextSearch(request.getSearchQuery().trim(), pageable);
        }

        // Verwende die Criteria-basierte Suche für Filter
        return searchAdvisorsWithCriteria(request, pageable);
    }

    private Page<Advisor> searchAdvisorsWithCriteria(AdvisorSearchRequest request, Pageable pageable) {
        Query query = buildSearchQuery(request);

        System.out.println("searchAdvisorsWithCriteria: " +  query);

        // Count total results
        long total = mongoTemplate.count(query, Advisor.class);

        // Apply pagination
        query.with(pageable);

        // Execute query
        List<Advisor> advisors = mongoTemplate.find(query, Advisor.class);

        return new PageImpl<>(advisors, pageable, total);
    }

    private Query buildSearchQuery(AdvisorSearchRequest request) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        String specialization = request.getSpecialization();
        String language = request.getLanguage();
        Double minRating = request.getMinRating();
        Double maxFee = request.getMaxFee();
        Boolean available = request.getAvailableToday();

        // Specialization Filter (nur anwenden wenn nicht leer)
        if (specialization != null && !specialization.trim().isEmpty()) {
            criteriaList.add(Criteria.where("specialization").regex(specialization, "i"));
        }

        // Language Filter (nur anwenden wenn nicht leer)
        if (language != null && !language.trim().isEmpty()) {
            criteriaList.add(Criteria.where("languages").is(language));
        }

        // Rating Filter
        if (minRating != null) {
            criteriaList.add(Criteria.where("rating").gte(minRating));
        }

        // Fee Filter
        if (maxFee != null) {
            criteriaList.add(Criteria.where("consultationFee").lte(maxFee));
        }

        // Availability Filter
        if (available != null) {
            criteriaList.add(Criteria.where("available").is(available));
        }

        // Combine criteria
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        return query;
    }


    private Page<Advisor> executeSearch2(AdvisorSearchRequest request, Pageable pageable) {
        // Wenn ein Suchbegriff vorhanden ist, verwende die Text-Suche
        if (request.getSearchQuery() != null && !request.getSearchQuery().trim().isEmpty()) {
            return advisorRepository.fullTextSearch(request.getSearchQuery().trim(), pageable);
        }

        // Ansonsten verwende die gefilterte Suche
        String specialization = request.getSpecialization() != null && !request.getSpecialization().isEmpty()
                ? request.getSpecialization()
                : null;

        String language = request.getLanguage() != null && !request.getLanguage().isEmpty()
                ? request.getLanguage()
                : null;

        Double minRating = request.getMinRating() != null ? request.getMinRating() : 0.0;
        Double maxFee = request.getMaxFee() != null ? request.getMaxFee() : Double.MAX_VALUE;
        Boolean available = request.getAvailableToday() != null ? request.getAvailableToday() : null;

        return advisorRepository.searchAdvisors(
                null, // searchQuery
                specialization,
                language,
                minRating,
                maxFee,
                available,
                pageable
        );
    }

    private AdvisorSearchResponse buildSearchResponse(Page<Advisor> advisorPage, AdvisorSearchRequest request) {
        List<Advisor> advisors = advisorPage.getContent();

        // Berater-Statistiken berechnen
        AdvisorSearchResponse.SearchStats stats = calculateSearchStats(request);

        return AdvisorSearchResponse.builder()
                .advisors(advisors)
                .totalCount(advisorPage.getTotalElements())
                .currentPage(advisorPage.getNumber())
                .totalPages(advisorPage.getTotalPages())
                .hasNext(advisorPage.hasNext())
                .hasPrevious(advisorPage.hasPrevious())
                .searchStats(stats)
                .build();
    }

    private AdvisorSearchResponse.SearchStats calculateSearchStats(AdvisorSearchRequest request) {
        Long totalAdvisors = advisorRepository.count();

        // Verfügbare Berater heute zählen
        Long availableToday = advisorRepository.findAvailableAdvisors(
                request.getMinRating() != null ? request.getMinRating() : 0.0,
                request.getMaxFee() != null ? request.getMaxFee() : Double.MAX_VALUE
        ).stream().count();

        // Durchschnittliche Bewertung und Honorar berechnen
        List<Advisor> allAdvisors = advisorRepository.findAll();
        double averageRating = allAdvisors.stream()
                .mapToDouble(Advisor::getRating)
                .average()
                .orElse(0.0);

        double averageFee = allAdvisors.stream()
                .mapToDouble(Advisor::getConsultationFee)
                .average()
                .orElse(0.0);

        return AdvisorSearchResponse.SearchStats.builder()
                .totalAdvisors(totalAdvisors)
                .availableToday(availableToday)
                .averageRating(Math.round(averageRating * 10.0) / 10.0) // Auf eine Dezimalstelle runden
                .averageFee(Math.round(averageFee * 100.0) / 100.0) // Auf zwei Dezimalstellen runden
                .build();
    }

    public List<String> getAllSpecializations() {
        log.info("getAllSpecializations");
        return advisorRepository.findAll().stream()
                .map(Advisor::getSpecialization)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> getAllLanguages() {
        log.info("getAllLanguages");
        return advisorRepository.findAll().stream()
                .flatMap(advisor -> advisor.getLanguages().stream())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Advisor> getFeaturedAdvisors() {
        log.info("getFeaturedAdvisors");
        // Kombiniere Top-rated und meistbewertete Berater
        List<Advisor> topRated = advisorRepository.findTop5ByOrderByRatingDesc();
        List<Advisor> mostReviewed = advisorRepository.findTop5ByOrderByReviewCountDesc();

        // Duplikate entfernen und kombinieren
        return topRated.stream()
                .filter(advisor -> !mostReviewed.contains(advisor))
                .collect(Collectors.toList());
    }

    public List<Advisor> getSimilarAdvisors(String advisorId, String specialization) {
        log.info("getSimilarAdvisors: advisorId: {} ; specialization: {}", advisorId, specialization);
        return advisorRepository.findBySpecializationContainingIgnoreCase(specialization).stream()
                .filter(advisor -> !advisor.getId().equals(advisorId))
                .limit(4)
                .collect(Collectors.toList());
    }
}