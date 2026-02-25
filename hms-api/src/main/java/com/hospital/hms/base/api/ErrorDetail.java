package com.hospital.hms.base.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single error detail for validation or field-level errors
 * <p>
 * This class provides detailed information about what went wrong,
 * which field caused the issue, and what value was rejected.
 * The error code can be used by clients for internationalization
 * or specific error handling logic.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetail {

    /**
     * The field name that caused the error (e.g., "email", "title")
     * Null for general errors not tied to a specific field
     */
    private String field;

    /**
     * The value that was rejected
     * Useful for debugging and showing users what they entered
     */
    private Object rejectedValue;

    /**
     * Human-readable error message
     */
    private String message;

    /**
     * Machine-readable error code for client-side handling
     * Examples: FIELD_REQUIRED, INVALID_EMAIL_FORMAT, DUPLICATE_ENTRY
     */
    private String code;

    /**
     * Create a field-specific error
     */
    public static ErrorDetail fieldError(String field, Object rejectedValue, String message, String code) {
        return ErrorDetail.builder()
                .field(field)
                .rejectedValue(rejectedValue)
                .message(message)
                .code(code)
                .build();
    }

    /**
     * Create a general error not tied to a specific field
     */
    public static ErrorDetail generalError(String message, String code) {
        return ErrorDetail.builder()
                .message(message)
                .code(code)
                .build();
    }
}
