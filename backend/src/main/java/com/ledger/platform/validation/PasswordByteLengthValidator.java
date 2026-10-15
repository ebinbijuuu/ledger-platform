package com.ledger.platform.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.nio.charset.StandardCharsets;

public class PasswordByteLengthValidator
        implements ConstraintValidator<PasswordByteLength, String> {

    private static final int MAX_PASSWORD_BYTES = 72;

    @Override
    public boolean isValid(
            String password,
            ConstraintValidatorContext context
    ) {
        // Let @NotBlank handle null and blank passwords.
        if (password == null) {
            return true;
        }

        return password.getBytes(StandardCharsets.UTF_8).length
                <= MAX_PASSWORD_BYTES;
    }
}