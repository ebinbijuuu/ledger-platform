package com.ledger.platform.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends ApiException {
    public ConflictException(String safeMessage) {
        super(HttpStatus.CONFLICT, safeMessage);
    }
}
