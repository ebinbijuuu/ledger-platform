package com.ledger.platform.dto;

import java.time.Instant;
import java.util.Map;

/** A safe, consistent error representation returned by the API. */
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors) {

    public ApiError {
        fieldErrors = fieldErrors == null ? Map.of() : Map.copyOf(fieldErrors);
    }
}
