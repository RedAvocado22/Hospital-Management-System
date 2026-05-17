package com.hospital.hms.exam_queue.request;

import com.hospital.hms.base.request.PaginatedRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GetExamQueueRequest extends PaginatedRequest {
}
