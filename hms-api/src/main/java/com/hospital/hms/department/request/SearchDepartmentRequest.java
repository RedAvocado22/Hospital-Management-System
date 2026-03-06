package com.hospital.hms.department.request;

import com.hospital.hms.base.request.PaginatedRequest;
import com.hospital.hms.common.model.DateRange;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SearchDepartmentRequest extends PaginatedRequest {
    private String name;

    private Boolean isActive;

    private DateRange createdAtRange;

    private DateRange updatedAtRange;
}
