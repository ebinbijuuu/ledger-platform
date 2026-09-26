package com.ledger.platform;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ledger.platform.exception.ConflictException;
import com.ledger.platform.exception.GlobalExceptionHandler;
import com.ledger.platform.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new ErrorTestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void validationFailureIncludesFieldMessage() throws Exception {
        mockMvc.perform(post("/api/v1/test/validated")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\" \"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.fieldErrors.name").value("must not be blank"))
                .andExpect(jsonPath("$.path").value("/api/v1/test/validated"));
    }

    @Test
    void malformedJsonReturnsSafeBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/test/validated")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{bad json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request body is missing or malformed"));
    }

    @Test
    void methodParameterValidationIncludesParameterMessage() throws Exception {
        mockMvc.perform(get("/api/v1/test/validated-parameter").param("count", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.fieldErrors.count").value("must be greater than or equal to 1"));
    }

    @Test
    void unsupportedVerbReturnsMethodNotAllowed() throws Exception {
        mockMvc.perform(put("/api/v1/test/validated"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value(405))
                .andExpect(jsonPath("$.message").value("HTTP method is not supported for this endpoint"));
    }

    @Test
    void unsupportedContentTypeReturnsUnsupportedMediaType() throws Exception {
        mockMvc.perform(post("/api/v1/test/consumes-json")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("value"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.status").value(415))
                .andExpect(jsonPath("$.message").value("Content type is not supported"));
    }

    @Test
    void missingResourceAndConflictUseTheirStatuses() throws Exception {
        mockMvc.perform(get("/api/v1/test/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        mockMvc.perform(get("/api/v1/test/conflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void unexpectedFailureDoesNotExposeExceptionDetails() throws Exception {
        mockMvc.perform(get("/api/v1/test/unexpected"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"))
                .andExpect(jsonPath("$.trace").doesNotExist())
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.not(
                        org.hamcrest.Matchers.containsString("sensitive internal detail"))));
    }

    @RestController
    static class ErrorTestController {
        @PostMapping("/api/v1/test/validated")
        String validated(@Valid @RequestBody TestRequest request) {
            return request.name();
        }

        @GetMapping("/api/v1/test/validated-parameter")
        String validatedParameter(@RequestParam @Min(1) int count) {
            return Integer.toString(count);
        }

        @PostMapping(path = "/api/v1/test/consumes-json", consumes = MediaType.APPLICATION_JSON_VALUE)
        String consumesJson(@RequestBody String body) {
            return body;
        }

        @GetMapping("/api/v1/test/missing")
        void missing() {
            throw new ResourceNotFoundException("Test resource was not found");
        }

        @GetMapping("/api/v1/test/conflict")
        void conflict() {
            throw new ConflictException("Test resource conflicts");
        }

        @GetMapping("/api/v1/test/unexpected")
        void unexpected() {
            throw new IllegalStateException("sensitive internal detail");
        }
    }

    record TestRequest(@NotBlank String name) {
    }
}
