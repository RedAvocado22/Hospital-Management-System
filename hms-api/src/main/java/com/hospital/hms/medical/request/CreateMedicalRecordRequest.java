package com.hospital.hms.medical.request;

import com.hospital.hms.base.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Request body for creating a new medical record after patient examination")
public class CreateMedicalRecordRequest extends BaseRequest {

    @NotNull(message = "Id are required")
    @Schema(
            description = "UUID of the patient being examined",
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID patientId;

    @Schema(
            description = "Doctor's advice or treatment instructions for the patient (optional at creation time)",
            example = "Rest for 3 days, avoid spicy food, return if fever exceeds 38.5°C"
    )
    private String advice;

    @NotBlank(message = "Description is required")
    @Schema(
            description = "Clinical notes from the examination — symptoms, findings, and diagnosis",
            example = "Patient presents with acute upper respiratory infection. Throat inflamed, mild fever.",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String description;
}
