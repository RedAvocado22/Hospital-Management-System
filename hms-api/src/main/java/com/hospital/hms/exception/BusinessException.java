package com.hospital.hms.exception;

import com.hospital.hms.base.exception.BaseException;
import org.springframework.http.HttpStatus;

public class BusinessException extends BaseException {
    public BusinessException(String message) {
        super(message, "BUSINESS_ERROR", HttpStatus.BAD_REQUEST);
    }
}
