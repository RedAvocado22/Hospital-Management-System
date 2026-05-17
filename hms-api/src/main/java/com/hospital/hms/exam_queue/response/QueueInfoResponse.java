package com.hospital.hms.exam_queue.response;

import com.hospital.hms.exam_queue.entity.QueueStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record QueueInfoResponse(
        UUID id,
        UUID patientId,
        LocalDate date,
        Integer position,
        QueueStatus status
) {
}
