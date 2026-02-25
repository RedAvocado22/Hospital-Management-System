package com.hospital.hms.exception;

import com.hospital.hms.base.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    public NotFoundException(String resource, Object id) {
        super(resource + " not found with slug: " + id,
                "RESOURCE_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}