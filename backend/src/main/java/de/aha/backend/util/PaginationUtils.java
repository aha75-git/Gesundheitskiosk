package de.aha.backend.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtils {

    private PaginationUtils() {
        // Utility class
    }

    public static Pageable createPageable(Integer page, Integer size, String sortBy, String direction) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? Math.min(size, 50) : 12; // Max 50 items per page

        if (sortBy != null) {
            Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction)
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            return PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, sortBy));
        }

        return PageRequest.of(pageNumber, pageSize);
    }

    public static void validatePaginationParams(Integer page, Integer size) {
        if (page != null && page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }
        if (size != null && (size <= 0 || size > 100)) {
            throw new IllegalArgumentException("Page size must be between 1 and 100");
        }
    }
}