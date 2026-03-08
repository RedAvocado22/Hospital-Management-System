package com.hospital.hms.employee.request;

import com.hospital.hms.base.request.BaseRequest;
import com.hospital.hms.common.enums.Gender;
import com.hospital.hms.exception.ValidationException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Request payload for update employee")
public class UpdateEmployeeRequest extends BaseRequest {
    @Schema(description = "Employee's id")
    private UUID id;

    @Schema(description = "Employee's first name", example = "Minh")
    private String firstName;

    @Schema(description = "Employee's last name", example = "Nguyen")
    private String lastName;

    @Schema(description = "Employee's birthday")
    private LocalDate dob;

    @Schema(description = "Employee's gender")
    private Gender gender;

    @Schema(description = "Employee's address")
    private String address;

    @Schema(description = "Employee's phone")
    private String phone;

    @Schema(description = "Role name to assign (e.g. DOCTOR, NURSE, RECEPTIONIST)", example = "DOCTOR")
    private String role;

    @Schema(description = "Department name to assign the employee to", example = "Cardiology")
    private String department;

    @Schema(description = "Unique employee code", example = "EMP-001")
    private String code;

    @Schema(description = "Employment start date", example = "2026-03-01")
    private LocalDate hireDate;

    @Override
    public void validate() {
        if (id == null) {
            throw new ValidationException("Employee ID is required for updates.");
        }

        validateString(firstName, "First name", 1, 50);
        validateString(lastName, "Last name", 1, 50);

        if (phone != null && !phone.matches("^\\+?[0-9]{10,15}$")) {
            throw new ValidationException("Invalid phone number format.");
        }

        if (dob != null && dob.isAfter(LocalDate.now().minusYears(18))) {
            throw new ValidationException("Employee must be at least 18 years old.");
        }

        if (hireDate != null && dob != null && hireDate.isBefore(dob)) {
            throw new ValidationException("Hire date cannot be before the date of birth.");
        }

        if (role != null && role.trim().isEmpty()) {
            throw new ValidationException("Role name cannot be blank.");
        }
    }

    private void validateString(String value, String fieldName, int min, int max) {
        if (value != null) {
            if (value.trim().isEmpty()) {
                throw new ValidationException(fieldName + " cannot be empty.");
            }
            if (value.length() < min || value.length() > max) {
                throw new ValidationException(fieldName + " must be between " + min + " and " + max + " characters.");
            }
        }
    }
}
