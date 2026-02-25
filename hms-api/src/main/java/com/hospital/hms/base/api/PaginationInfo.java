package com.hospital.hms.base.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standardized pagination information
 * <p>
 * This class provides all the information clients need to
 * navigate through paginated results, including current position,
 * total items, and whether more pages are available.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationInfo {

    /**
     * Current page number (0-indexed or 1-indexed based on your preference)
     * Example: 0 for first page, 1 for second page
     */
    private int page;

    /**
     * Number of items per page
     * Example: 20
     */
    private int size;

    /**
     * Total number of items across all pages
     * Example: 157
     */
    private long totalElements;

    /**
     * Total number of pages
     * Example: 8 (if 157 items with 20 per page)
     */
    private int totalPages;

    /**
     * Whether there is a next page available
     */
    private boolean hasNext;

    /**
     * Whether there is a previous page available
     */
    private boolean hasPrevious;

    /**
     * Whether this is the first page
     */
    private boolean isFirst;

    /**
     * Whether this is the last page
     */
    private boolean isLast;

    /**
     * Create pagination info from Spring Data Page object
     */
    public static PaginationInfo fromPage(org.springframework.data.domain.Page<?> page) {
        return PaginationInfo.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .build();
    }
}
