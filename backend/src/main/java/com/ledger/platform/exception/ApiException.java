package com.ledger.platform.exception;

import org.springframework.http.HttpStatus;

/** Base type for expected API errors with a message safe to return to clients. */
public abstract class ApiException extends RuntimeException {
    private final HttpStatus status;

    protected ApiException(HttpStatus status, String safeMessage) {
        super(safeMessage);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
