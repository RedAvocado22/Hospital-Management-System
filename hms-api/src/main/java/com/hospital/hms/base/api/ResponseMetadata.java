package com.hospital.hms.base.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Metadata about the API request and response
 * <p>
 * This information is crucial for debugging, monitoring, and tracing
 * requests across distributed systems. The trace ID can be used to
 * correlate logs across multiple services.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMetadata {

    /**
     * Unique identifier for this request
     * Used for distributed tracing and log correlation
     * Format: UUID (e.g., "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
     */
    private String traceId;

    /**
     * The request path that was called
     * Example: "/api/v1/courses"
     */
    private String path;

    /**
     * HTTP method used
     * Example: "POST", "GET", "PUT", "DELETE"
     */
    private String method;

    /**
     * Processing time in milliseconds
     * Useful for performance monitoring and optimization
     */
    private Long duration;

    /**
     * API version
     * Example: "v1", "v2"
     */
    private String apiVersion;

    /**
     * Optional: Client IP address (for security auditing)
     */
    private String clientIp;

    /**
     * Optional: User agent string
     */
    private String userAgent;
}
