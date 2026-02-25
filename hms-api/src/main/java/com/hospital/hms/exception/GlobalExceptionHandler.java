package com.hospital.hms.exception;

import com.hospital.hms.base.api.ApiResponse;
import com.hospital.hms.base.api.ErrorDetail;
import com.hospital.hms.base.api.ResponseMetadata;
import com.hospital.hms.base.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.UUID;

/**
 * Global exception handler for consistent API error responses.
 * <p>
 * Intercepts all exceptions thrown by controllers and converts them
 * into standardized {@link ApiResponse} objects with proper error details,
 * metadata, and HTTP status codes.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==================== Bean Validation ====================

    /**
     * Handle Bean Validation errors (from @Valid annotation).
     * Triggered when request body validation fails (@NotBlank, @Size, @Email, etc.)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String traceId = generateTraceId();
        log.error("[TraceID: {}] Validation error on {}: {}",
                traceId, request.getRequestURI(), ex.getMessage());

        List<ErrorDetail> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toErrorDetail)
                .toList();

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", errors, traceId, request);
    }

    // ==================== Custom Application Exceptions ====================

    /**
     * Handle all BaseException subclasses (NotFoundException, BusinessException, ValidationException).
     * Uses the httpStatus and errorCode stored in the exception itself.
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Object>> handleBaseException(
            BaseException ex,
            HttpServletRequest request) {

        String traceId = generateTraceId();
        HttpStatus status = ex.getHttpStatus();

        log.error("[TraceID: {}] {} on {}: {}",
                traceId, ex.getErrorCode(), request.getRequestURI(), ex.getMessage());

        ErrorDetail error = ErrorDetail.generalError(ex.getMessage(), ex.getErrorCode());
        return buildErrorResponse(status, ex.getMessage(), List.of(error), traceId, request);
    }

    // ==================== Framework Exceptions ====================

    /**
     * Handle business logic validation errors via IllegalArgumentException.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        String traceId = generateTraceId();
        log.error("[TraceID: {}] Business validation error on {}: {}",
                traceId, request.getRequestURI(), ex.getMessage());

        ErrorDetail error = ErrorDetail.generalError(ex.getMessage(), "BUSINESS_VALIDATION_ERROR");
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), List.of(error), traceId, request);
    }

    /**
     * Handle malformed JSON or request body parsing errors.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        String traceId = generateTraceId();
        log.error("[TraceID: {}] Malformed request on {}: {}",
                traceId, request.getRequestURI(), ex.getMessage());

        ErrorDetail error = ErrorDetail.generalError(
                "Malformed JSON request. Please check your request body format.",
                "MALFORMED_REQUEST"
        );
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid request format", List.of(error), traceId, request);
    }

    // ==================== Catch-All ====================

    /**
     * Handle all other unexpected exceptions.
     * Ensures even unknown errors return a consistent response format.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        String traceId = generateTraceId();
        log.error("[TraceID: {}] Unexpected error on {}: {}",
                traceId, request.getRequestURI(), ex.getMessage(), ex);

        ErrorDetail error = ErrorDetail.generalError(
                "An unexpected error occurred. Please contact support with trace ID: " + traceId,
                "INTERNAL_SERVER_ERROR"
        );
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", List.of(error), traceId, request);
    }

    // ==================== Private Helpers ====================

    /**
     * Build a standardized error ResponseEntity — single place for all error formatting.
     */
    private ResponseEntity<ApiResponse<Object>> buildErrorResponse(
            HttpStatus status,
            String message,
            List<ErrorDetail> errors,
            String traceId,
            HttpServletRequest request) {

        ResponseMetadata metadata = ResponseMetadata.builder()
                .traceId(traceId)
                .path(request.getRequestURI())
                .method(request.getMethod())
                .apiVersion("v1")
                .build();

        ApiResponse<Object> response = ApiResponse.error(message, status.value(), errors, metadata);

        return ResponseEntity.status(status).body(response);
    }

    /**
     * Convert a Spring FieldError to our ErrorDetail.
     */
    private ErrorDetail toErrorDetail(FieldError fieldError) {
        return ErrorDetail.fieldError(
                fieldError.getField(),
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage(),
                "VALIDATION_ERROR"
        );
    }

    private String generateTraceId() {
        return UUID.randomUUID().toString();
    }
}
