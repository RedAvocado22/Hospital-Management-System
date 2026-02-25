package com.hospital.hms.base.exception;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus httpStatus;

    public BaseException(String message, String errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
