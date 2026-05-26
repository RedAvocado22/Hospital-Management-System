package com.hospital.hms.exam_queue.request;

import com.hospital.hms.base.request.BaseRequest;
import com.hospital.hms.patient.entity.PatientInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Request body for creating a new queue record after patient examination")
public class AddToQueueRequest extends BaseRequest {

    @NotNull(message = "Patient is required")
    @Schema(
            description = "Patient entity being examined",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private PatientInfo patient;

}
