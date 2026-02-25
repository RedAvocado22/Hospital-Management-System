package com.hospital.hms.base.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API Response wrapper for all API endpoints
 * <p>
 * This class provides a standardized response structure across all endpoints,
 * including success responses, error responses, validation errors, metadata,
 * pagination, and HATEOAS links.
 * <p>
 * Example Success Response:
 * <pre>
 * {
 *   "status": "success",
 *   "message": "Course created successfully",
 *   "code": 201,
 *   "data": { ... },
 *   "metadata": {
 *     "traceId": "a1b2c3d4-...",
 *     "path": "/api/v1/courses",
 *     "method": "POST",
 *     "duration": 45
 *   },
 *   "links": [
 *     { "rel": "self", "href": "/api/v1/courses/123", "method": "GET" }
 *   ],
 *   "timestamp": "2026-02-05T06:53:17Z"
 * }
 * </pre>
 * <p>
 * Example Error Response:
 * <pre>
 * {
 *   "status": "error",
 *   "message": "Validation failed",
 *   "code": 400,
 *   "errors": [
 *     {
 *       "field": "title",
 *       "rejectedValue": "",
 *       "message": "Title is required",
 *       "code": "FIELD_REQUIRED"
 *     }
 *   ],
 *   "metadata": { ... },
 *   "timestamp": "2026-02-05T06:53:17Z"
 * }
 * </pre>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {

    /**
     * Response status: "success" or "error"
     */
    private String status;

    /**
     * Human-readable message describing the result
     */
    private String message;

    /**
     * HTTP status code (200, 201, 400, 404, 500, etc.)
     */
    private int code;

    /**
     * The actual response data (null for errors)
     */
    private T data;

    /**
     * List of detailed errors (for validation failures)
     */
    @Builder.Default
    private List<ErrorDetail> errors = new ArrayList<>();

    /**
     * Request metadata (trace ID, path, method, duration, etc.)
     */
    private ResponseMetadata metadata;

    /**
     * Pagination information (for paginated endpoints)
     */
    private PaginationInfo pagination;

    /**
     * HATEOAS links to related resources
     */
    @Builder.Default
    private List<Link> links = new ArrayList<>();

    /**
     * Additional metadata as key-value pairs
     * Useful for custom fields without changing the class structure
     */
    @Builder.Default
    private Map<String, Object> additionalInfo = new HashMap<>();

    /**
     * Response timestamp
     */
    @Builder.Default
    private Instant timestamp = Instant.now();

    // ==================== Helper Methods ====================

    /**
     * Create a success response with data
     */
    public static <T> ApiResponse<T> success(T data, String message, int code) {
        return ApiResponse.<T>builder()
                .status("success")
                .message(message)
                .code(code)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    /**
     * Create a success response with data and metadata
     */
    public static <T> ApiResponse<T> success(T data, String message, int code, ResponseMetadata metadata) {
        return ApiResponse.<T>builder()
                .status("success")
                .message(message)
                .code(code)
                .data(data)
                .metadata(metadata)
                .timestamp(Instant.now())
                .build();
    }

    /**
     * Create an error response with a single error detail
     */
    public static <T> ApiResponse<T> error(String message, int code, ErrorDetail error) {
        List<ErrorDetail> errors = new ArrayList<>();
        if (error != null) {
            errors.add(error);
        }
        return ApiResponse.<T>builder()
                .status("error")
                .message(message)
                .code(code)
                .data(null)
                .errors(errors)
                .timestamp(Instant.now())
                .build();
    }

    /**
     * Create an error response with multiple error details
     */
    public static <T> ApiResponse<T> error(String message, int code, List<ErrorDetail> errors) {
        return ApiResponse.<T>builder()
                .status("error")
                .message(message)
                .code(code)
                .data(null)
                .errors(errors != null ? errors : new ArrayList<>())
                .timestamp(Instant.now())
                .build();
    }

    /**
     * Create an error response with metadata
     */
    public static <T> ApiResponse<T> error(String message, int code, List<ErrorDetail> errors, ResponseMetadata metadata) {
        return ApiResponse.<T>builder()
                .status("error")
                .message(message)
                .code(code)
                .data(null)
                .errors(errors != null ? errors : new ArrayList<>())
                .metadata(metadata)
                .timestamp(Instant.now())
                .build();
    }

    /**
     * Create a simple error response without detailed errors
     */
    public static <T> ApiResponse<T> simpleError(String message, int code) {
        return ApiResponse.<T>builder()
                .status("error")
                .message(message)
                .code(code)
                .data(null)
                .timestamp(Instant.now())
                .build();
    }

    // ==================== Fluent API Methods ====================

    /**
     * Add a single error detail
     */
    public ApiResponse<T> addError(ErrorDetail error) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(error);
        return this;
    }

    /**
     * Add a HATEOAS link
     */
    public ApiResponse<T> addLink(Link link) {
        if (this.links == null) {
            this.links = new ArrayList<>();
        }
        this.links.add(link);
        return this;
    }

    /**
     * Add a HATEOAS link with rel and href
     */
    public ApiResponse<T> addLink(String rel, String href) {
        return addLink(Link.of(rel, href));
    }

    /**
     * Add a HATEOAS link with rel, href, and method
     */
    public ApiResponse<T> addLink(String rel, String href, String method) {
        return addLink(Link.of(rel, href, method));
    }

    /**
     * Add additional metadata
     */
    public ApiResponse<T> addInfo(String key, Object value) {
        if (this.additionalInfo == null) {
            this.additionalInfo = new HashMap<>();
        }
        this.additionalInfo.put(key, value);
        return this;
    }

    /**
     * Set pagination info
     */
    public ApiResponse<T> withPagination(PaginationInfo pagination) {
        this.pagination = pagination;
        return this;
    }

    /**
     * Set metadata
     */
    public ApiResponse<T> withMetadata(ResponseMetadata metadata) {
        this.metadata = metadata;
        return this;
    }
}
