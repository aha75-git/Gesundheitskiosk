package de.aha.backend.dto.advisor;

import de.aha.backend.model.advisor.Advisor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvisorSearchResponse {
    private List<Advisor> advisors;
    private Long totalCount;
    private Integer currentPage;
    private Integer totalPages;
    private Boolean hasNext;
    private Boolean hasPrevious;

    // Suchstatistiken
    private SearchStats searchStats;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchStats {
        private Long totalAdvisors;
        private Long availableToday;
        private Double averageRating;
        private Double averageFee;
    }
}