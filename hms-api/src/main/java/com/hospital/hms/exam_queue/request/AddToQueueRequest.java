package com.hospital.hms.exam_queue.request;

import com.hospital.hms.base.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Request body for creating a new queue record after patient examination")
public class AddToQueueRequest extends BaseRequest {

    @NotNull(message = "Id are required")
    @Schema(
            description = "UUID of the patient being examined",
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID patientId;

}
