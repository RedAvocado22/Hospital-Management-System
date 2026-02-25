package com.hospital.hms.base.request;

import com.hospital.hms.base.UserContext;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseRequest {

    /**
     * Request ID for tracking
     */
    private UUID requestId;

    /**
     * User context (set by framework)
     */
    private UserContext userContext;

    /**
     * Timestamp when request was created
     */
    private LocalDateTime createdAt;

    /**
     * Custom validation logic
     * Override this in subclasses for business validation
     */
    public void validate() {
    }

    /**
     * Convert to log-safe string (hide sensitive data)
     */
    public String toLogString() {
        return String.format("%s[requestId=%s]",
                this.getClass().getSimpleName(),
                requestId
        );
    }

    /**
     * Initialize request (called before processing)
     */
    public void initialize() {
        if (requestId == null) {
            requestId = UUID.randomUUID();
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}