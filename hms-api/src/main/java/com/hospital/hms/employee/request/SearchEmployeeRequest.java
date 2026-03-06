package com.hospital.hms.employee.request;

import com.hospital.hms.base.request.PaginatedRequest;
import com.hospital.hms.common.enums.Gender;
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
public class SearchEmployeeRequest extends PaginatedRequest {
    private String name;

    private DateRange dob;

    private Gender gender;

    private String address;

    private String phone;

    private String email;

    private DateRange hireDate;

    private DateRange createdAtRange;

    private DateRange updatedAtRange;

    private String department;

    private String code;

    private Boolean isActive;
}
