package com.hospital.hms.base.service;

import com.hospital.hms.base.request.BaseRequest;
import com.hospital.hms.exception.BusinessException;
import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.exception.ValidationException;

import java.util.Optional;

/**
 * Unified service interface for ALL operations (CRUD and non-CRUD).
 * <p>
 * Every operation follows the same pipeline:
 * <pre>
 *   Controller → execute(request) → validate → doProcess → response
 * </pre>
 * <p>
 * This gives you a single intercept point for logging, auditing, validation,
 * and performance tracking across every service in the system.
 *
 * <h3>Usage examples:</h3>
 * <pre>
 * // CRUD — find by ID
 * public class FindPatientByIdService implements BaseService&lt;FindByIdRequest, PatientResponse&gt; {
 *     public PatientResponse doProcess(FindByIdRequest req) { ... }
 * }
 *
 * // CRUD — delete (returns Void)
 * public class DeletePatientService implements BaseService&lt;DeleteRequest, Void&gt; {
 *     public Void doProcess(DeleteRequest req) { ...; return null; }
 * }
 *
 * // Non-CRUD — send email
 * public class SendReportEmailService implements BaseService&lt;SendReportRequest, SendReportResponse&gt; {
 *     public SendReportResponse doProcess(SendReportRequest req) { ... }
 * }
 * </pre>
 *
 * @param <REQ> Request type (must extend BaseRequest)
 * @param <RES> Response type
 */
public interface BaseService<REQ extends BaseRequest, RES> {

    /**
     * Core processing logic — implement this in each service.
     * <p>
     * This method is called AFTER validation passes.
     * Do NOT call this directly — use {@link #execute(BaseRequest)} instead.
     *
     * @param request The validated, initialized request
     * @return The response (return {@code null} for Void operations)
     * @throws NotFoundException   if a required resource is not found
     * @throws BusinessException   if a business rule is violated
     * @throws ValidationException if custom validation fails inside processing
     */
    RES doProcess(REQ request);

    /**
     * Validate the request before processing.
     * <p>
     * Default behavior:
     * <ol>
     *   <li>Null check</li>
     *   <li>Calls {@code request.validate()} for custom business validation</li>
     * </ol>
     * Override to add service-level validation (e.g., checking DB constraints).
     *
     * @param request The request to validate
     * @throws IllegalArgumentException if request is null
     * @throws ValidationException      if validation fails
     */
    default void validate(REQ request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        request.validate();
    }

    /**
     * Main entry point — controllers should call this.
     * <p>
     * Pipeline: initialize → validate → doProcess
     *
     * @param request The request object
     * @return The response
     */
    default RES execute(REQ request) {
        // 1. Initialize (auto-set requestId, timestamp if missing)
        if (request != null) {
            request.initialize();
        }
        // 2. Validate
        validate(request);
        // 3. Process
        return doProcess(request);
    }

    /**
     * Execute and return Optional — for lookups that may return nothing.
     * <p>
     * Catches {@link NotFoundException} and returns empty instead of throwing.
     *
     * @param request The request object
     * @return Optional response (empty if not found)
     */
    default Optional<RES> executeOptional(REQ request) {
        try {
            return Optional.ofNullable(execute(request));
        } catch (NotFoundException e) {
            return Optional.empty();
        }
    }

    /**
     * Execute silently — swallows exceptions and returns empty.
     * <p>
     * Use for fire-and-forget operations (e.g., sending a non-critical notification)
     * where failure should be logged but not propagated.
     *
     * @param request The request object
     * @return Optional response (empty on any failure)
     */
    default Optional<RES> executeSilent(REQ request) {
        try {
            return Optional.ofNullable(execute(request));
        } catch (Exception e) {
            // Subclasses with @Slf4j can override to add logging
            return Optional.empty();
        }
    }
}
