package com.hospital.hms.exception;

import com.hospital.hms.base.exception.BaseException;
import org.springframework.http.HttpStatus;

public class IdentityProviderException extends BaseException {

    public IdentityProviderException(String message) {
        super(message, "IDENTITY_PROVIDER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public IdentityProviderException(String message, HttpStatus status) {
        super(message, "IDENTITY_PROVIDER_ERROR", status);
    }

    public IdentityProviderException(String message, String errorCode, HttpStatus status) {
        super(message, errorCode, status);
    }
}
