package com.ledger.platform.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ledger.platform.dto.ApiError;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

public class FilterChainExceptionHandler implements Filter {

    private static final Logger logger =
            LoggerFactory.getLogger(FilterChainExceptionHandler.class);

    private final ObjectMapper objectMapper;

    public FilterChainExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            chain.doFilter(request, response);
        } catch (Exception exception) {
            if (httpResponse.isCommitted()) {
                rethrow(exception);
                return;
            }

            logger.error(
                    "Unexpected exception while processing request {} {}",
                    httpRequest.getMethod(),
                    httpRequest.getRequestURI(),
                    exception
            );

            httpResponse.resetBuffer();
            httpResponse.setStatus(
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");

            ApiError error = new ApiError(
                    Instant.now(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    "An unexpected error occurred",
                    httpRequest.getRequestURI(),
                    Map.of()
            );

            objectMapper.writeValue(
                    httpResponse.getOutputStream(),
                    error
            );
        }
    }

    private void rethrow(Exception exception)
            throws IOException, ServletException {

        if (exception instanceof IOException ioException) {
            throw ioException;
        }

        if (exception instanceof ServletException servletException) {
            throw servletException;
        }

        if (exception instanceof RuntimeException runtimeException) {
            throw runtimeException;
        }

        throw new ServletException(exception);
    }
}