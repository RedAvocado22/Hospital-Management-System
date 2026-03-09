package com.hospital.hms.employee.request;

import com.hospital.hms.base.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Request payload for registering a new employee")
public class CreateEmployeeRequest extends BaseRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Schema(description = "Unique login username", example = "dr.nguyen", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "Employee email address", example = "nguyen@hospital.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Schema(description = "Account password (min 6 chars)", example = "P@ssw0rd!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotBlank(message = "First name is required")
    @Schema(description = "Employee's first name", example = "Minh", requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Schema(description = "Employee's last name", example = "Nguyen", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @NotBlank(message = "Role is required")
    @Schema(description = "Role name to assign (e.g. DOCTOR, NURSE, RECEPTIONIST)", example = "DOCTOR", requiredMode = Schema.RequiredMode.REQUIRED)
    private String role;

    @Schema(description = "Department name to assign the employee to", example = "Cardiology")
    private String department;

    @Schema(description = "Unique employee code", example = "EMP-001")
    private String code;

    @Schema(description = "Employment start date", example = "2026-03-01")
    private LocalDate hireDate;
}
