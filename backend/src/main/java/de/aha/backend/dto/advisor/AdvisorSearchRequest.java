package de.aha.backend.dto.advisor;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
@Builder
public class AdvisorSearchRequest {
    private String searchQuery;
    private String specialization;
    private String language;
    private Double minRating;
    private Double maxFee;
    private Boolean availableToday;
    private String sortBy;
    private Sort.Direction sortDirection;
    private Integer page = 0;
    private Integer size = 12;

    // Getter für Pagination
    public Integer getPage() {
        return page != null ? page : 0;
    }

    public Integer getSize() {
        return size != null ? size : 12;
    }
}
