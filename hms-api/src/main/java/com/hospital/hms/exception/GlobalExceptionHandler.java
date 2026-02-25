package com.hospital.hms.exception;

import com.hospital.hms.base.api.ApiResponse;
import com.hospital.hms.base.api.ErrorDetail;
import com.hospital.hms.base.api.ResponseMetadata;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Global exception handler for consistent API error responses
 * <p>
 * This class intercepts all exceptions thrown by controllers and
 * converts them into standardized ApiResponse objects with proper
 * error details, metadata, and HTTP status codes.
 * <p>
 * Benefits:
 * - DRY principle: No need for try-catch blocks in controllers
 * - Consistent error response format across all endpoints
 * - Automatic validation error details
 * - Request tracing with unique trace IDs
 * - Better separation of concerns
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle Bean Validation errors (from @Valid annotation)
     * <p>
     * This is triggered when request body validation fails
     * (e.g., @NotBlank, @Size, @Email, etc.)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();
        log.error("[TraceID: {}] Validation error on {}: {}",
                traceId, request.getRequestURI(), ex.getMessage());

        List<ErrorDetail> errors = new ArrayList<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            ErrorDetail error = ErrorDetail.builder()
                    .field(fieldError.getField())
                    .rejectedValue(fieldError.getRejectedValue())
                    .message(fieldError.getDefaultMessage())
                    .code("VALIDATION_ERROR")
                    .build();
            errors.add(error);
        }

        ResponseMetadata metadata = ResponseMetadata.builder()
                .traceId(traceId)
                .path(request.getRequestURI())
                .method(request.getMethod())
                .apiVersion("v1")
                .build();

        ApiResponse<Object> response = ApiResponse.error(
                "Validation failed",
                HttpStatus.BAD_REQUEST.value(),
                errors,
                metadata
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * Handle business logic validation errors
     * <p>
     * This is triggered when service-level validation fails
     * (e.g., duplicate course title, unsupported language, etc.)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();
        log.error("[TraceID: {}] Business validation error on {}: {}",
                traceId, request.getRequestURI(), ex.getMessage());

        ErrorDetail error = ErrorDetail.builder()
                .message(ex.getMessage())
                .code("BUSINESS_VALIDATION_ERROR")
                .build();

        ResponseMetadata metadata = ResponseMetadata.builder()
                .traceId(traceId)
                .path(request.getRequestURI())
                .method(request.getMethod())
                .apiVersion("v1")
                .build();

        ApiResponse<Object> response = ApiResponse.error(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                error
        ).withMetadata(metadata);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * Handle malformed JSON or request body parsing errors
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();
        log.error("[TraceID: {}] Malformed request on {}: {}",
                traceId, request.getRequestURI(), ex.getMessage());

        ErrorDetail error = ErrorDetail.builder()
                .message("Malformed JSON request. Please check your request body format.")
                .code("MALFORMED_REQUEST")
                .build();

        ResponseMetadata metadata = ResponseMetadata.builder()
                .traceId(traceId)
                .path(request.getRequestURI())
                .method(request.getMethod())
                .apiVersion("v1")
                .build();

        ApiResponse<Object> response = ApiResponse.error(
                "Invalid request format",
                HttpStatus.BAD_REQUEST.value(),
                error
        ).withMetadata(metadata);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * Handle custom NotFoundException
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFoundException(
            NotFoundException ex,
            HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();
        log.error("[TraceID: {}] Resource not found on {}: {}",
                traceId, request.getRequestURI(), ex.getMessage());

        ErrorDetail error = ErrorDetail.builder()
                .message(ex.getMessage())
                .code("RESOURCE_NOT_FOUND")
                .build();

        ResponseMetadata metadata = ResponseMetadata.builder()
                .traceId(traceId)
                .path(request.getRequestURI())
                .method(request.getMethod())
                .apiVersion("v1")
                .build();

        ApiResponse<Object> response = ApiResponse.error(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                error
        ).withMetadata(metadata);

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    /**
     * Handle custom BusinessException
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();
        log.error("[TraceID: {}] Business error on {}: {}",
                traceId, request.getRequestURI(), ex.getMessage());

        ErrorDetail error = ErrorDetail.builder()
                .message(ex.getMessage())
                .code("BUSINESS_ERROR")
                .build();

        ResponseMetadata metadata = ResponseMetadata.builder()
                .traceId(traceId)
                .path(request.getRequestURI())
                .method(request.getMethod())
                .apiVersion("v1")
                .build();

        ApiResponse<Object> response = ApiResponse.error(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                error
        ).withMetadata(metadata);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * Handle custom ValidationException
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(
            ValidationException ex,
            HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();
        log.error("[TraceID: {}] Validation error on {}: {}",
                traceId, request.getRequestURI(), ex.getMessage());

        ErrorDetail error = ErrorDetail.builder()
                .message(ex.getMessage())
                .code("VALIDATION_ERROR")
                .build();

        ResponseMetadata metadata = ResponseMetadata.builder()
                .traceId(traceId)
                .path(request.getRequestURI())
                .method(request.getMethod())
                .apiVersion("v1")
                .build();

        ApiResponse<Object> response = ApiResponse.error(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                error
        ).withMetadata(metadata);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * Handle all other unexpected exceptions
     * <p>
     * This is the catch-all handler for any exceptions not handled above.
     * It ensures that even unexpected errors return a consistent response format.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        String traceId = UUID.randomUUID().toString();
        log.error("[TraceID: {}] Unexpected error on {}: {}",
                traceId, request.getRequestURI(), ex.getMessage(), ex);

        ErrorDetail error = ErrorDetail.builder()
                .message("An unexpected error occurred. Please contact support with trace ID: " + traceId)
                .code("INTERNAL_SERVER_ERROR")
                .build();

        ResponseMetadata metadata = ResponseMetadata.builder()
                .traceId(traceId)
                .path(request.getRequestURI())
                .method(request.getMethod())
                .apiVersion("v1")
                .build();

        ApiResponse<Object> response = ApiResponse.error(
                "Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                error
        ).withMetadata(metadata);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
