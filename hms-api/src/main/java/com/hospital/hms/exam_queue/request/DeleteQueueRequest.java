package com.hospital.hms.exam_queue.request;


import com.hospital.hms.base.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Request body for delete an existing queue. If status is DONE you cannot delete ")
public class DeleteQueueRequest extends BaseRequest {

    @Schema(hidden = true)
    private UUID id;
}
