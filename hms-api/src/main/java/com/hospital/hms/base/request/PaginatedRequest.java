package com.hospital.hms.base.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class PaginatedRequest extends BaseRequest {

    @Min(value = 0, message = "Page number cannot be negative")
    private Integer page = 0;

    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size cannot exceed 100")
    private Integer size = 20;

    private String sortBy;

    @Pattern(regexp = "^(ASC|DESC)$",
            message = "Sort direction must be ASC or DESC",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String sortDirection = "ASC";

    public Pageable toPageable() {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        if (sortBy != null && !sortBy.isBlank()) {
            Sort.Direction direction = "DESC".equalsIgnoreCase(sortDirection)
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            return PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        }

        return PageRequest.of(pageNumber, pageSize);
    }
}