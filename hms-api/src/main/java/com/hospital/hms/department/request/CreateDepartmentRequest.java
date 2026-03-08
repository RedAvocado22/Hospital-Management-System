package com.hospital.hms.department.request;

import com.hospital.hms.base.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Request payload for creating a new department")
public class CreateDepartmentRequest extends BaseRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    @Schema(description = "Department name (must be unique)", example = "Cardiology", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotNull(message = "Department's status is required")
    @Schema(description = "Whether the department is active", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean isActive;
}
