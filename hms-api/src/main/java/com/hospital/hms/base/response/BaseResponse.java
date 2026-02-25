package com.hospital.hms.base.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Base class for all response DTOs
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseResponse {

    /**
     * When this response was processed
     */
    private LocalDateTime processedAt;

    /**
     * Processing time in milliseconds
     */
    private Long processingTimeMs;

    /**
     * Response version (for API versioning)
     */
    private String version;

    /**
     * Convert to log-safe string
     */
    public String toLogString() {
        return String.format("%s[processedAt=%s, processingTime=%sms]",
                this.getClass().getSimpleName(),
                processedAt,
                processingTimeMs != null ? processingTimeMs : "N/A"
        );
    }
}
