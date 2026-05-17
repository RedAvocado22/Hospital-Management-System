package com.hospital.hms.exam_queue.entity;

import lombok.Getter;

@Getter
public enum QueueStatus {
    WAITING("Waiting for examination"),
    IN_PROGRESS("Examination in progress"),
    DONE("Examination completed"),
    CANCELLED("Examination cancelled");

    private final String description;

    QueueStatus(String description) {
        this.description = description;
    }

}
