package com.hospital.hms.base.service;

import com.hospital.hms.base.request.BaseRequest;
import com.hospital.hms.base.response.BaseResponse;
import com.hospital.hms.exception.BusinessException;
import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.exception.ValidationException;

import java.util.Optional;

/**
 * Base service interface with validation and error handling
 *
 * @param <REQ> Request type (must extend BaseRequest)
 * @param <RES> Response type (must extend BaseResponse)
 */
public interface BaseService<REQ extends BaseRequest, RES extends BaseResponse> {
    /**
     * Main processing method
     *
     * @param request The request object
     * @return The response object
     * @throws ValidationException if request validation fails
     * @throws BusinessException   if business logic fails
     */
    RES doProcess(REQ request);

    /**
     * Validate request before processing
     * Override this to add custom validation
     *
     * @param request The request to validate
     * @throws ValidationException if validation fails
     */
    default void validate(REQ request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        request.validate();
    }

    /**
     * Process with automatic validation
     *
     * @param request The request object
     * @return The response object
     */
    default RES execute(REQ request) {
        validate(request);
        return doProcess(request);
    }

    /**
     * Process and return Optional (for operations that might not return data)
     *
     * @param request The request object
     * @return Optional response
     */
    default Optional<RES> processOptional(REQ request) {
        try {
            return Optional.ofNullable(execute(request));
        } catch (NotFoundException e) {
            return Optional.empty();
        }
    }
}
