package com.ledger.platform.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException(String safeMessage) {
        super(HttpStatus.NOT_FOUND, safeMessage);
    }
}
