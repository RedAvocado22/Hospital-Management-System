package com.hospital.hms.exception;

import com.hospital.hms.base.exception.BaseException;
import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends BaseException {
    public DuplicateResourceException(String message) {
        super(message, "RESOURCE_EXISTED", HttpStatus.CONFLICT);
    }
}
