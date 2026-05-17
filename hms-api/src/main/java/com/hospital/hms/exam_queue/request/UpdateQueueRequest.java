package com.hospital.hms.exam_queue.request;

import com.hospital.hms.base.request.BaseRequest;
import com.hospital.hms.exam_queue.entity.QueueStatus;
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
@Schema(description = "Request body for updating an existing queue. Only status fields are applied (partial update).")
public class UpdateQueueRequest extends BaseRequest {

    @Schema(hidden = true)
    private UUID id;

    @Schema(
            description = "Updated status for the queue. Omit or send null to leave unchanged.",
            example = "IN_PROGRESS."
    )
    @NotNull
    private QueueStatus status;
}
