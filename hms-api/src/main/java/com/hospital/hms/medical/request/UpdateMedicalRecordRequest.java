package com.hospital.hms.medical.request;

import com.hospital.hms.base.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Request body for updating an existing medical record. Only non-null fields are applied (partial update).")
public class UpdateMedicalRecordRequest extends BaseRequest {

    @Schema(hidden = true)
    private UUID id;

    @Schema(
            description = "Updated doctor's advice for the patient. Omit or send null to leave unchanged.",
            example = "Follow up in 7 days if symptoms persist."
    )
    private String advice;

    @Schema(
            description = "Updated clinical notes for this record. Omit or send null to leave unchanged.",
            example = "Re-examined: condition improving, inflammation reduced."
    )
    private String description;
}
