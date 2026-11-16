package com.ledger.platform.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

@Entity
@Table(name = "accounts")
public class Account {

    private static final Pattern ACCOUNT_NUMBER_PATTERN =
            Pattern.compile("[A-Z0-9]{8,34}");

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "owner_subject", nullable = false, length = 255)
    private String ownerSubject;

    @Column(name = "account_number", nullable = false, unique = true, length = 34)
    private String accountNumber;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private AccountStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Account() {
        // Required by JPA.
    }

    private Account(
            UUID id,
            String ownerSubject,
            String accountNumber,
            String currency,
            AccountStatus status
    ) {
        this.id = id;
        this.ownerSubject = validateOwnerSubject(ownerSubject);
        this.accountNumber = validateAccountNumber(accountNumber);
        this.currency = validateCurrency(currency);
        this.status = Objects.requireNonNull(status, "Account status is required");
    }

    /** Creates an account after validating values that must be valid in every domain entry point. */
    public static Account open(
            String ownerSubject,
            String accountNumber,
            String currency,
            AccountStatus status
    ) {
        return new Account(
                UUID.randomUUID(),
                ownerSubject,
                accountNumber,
                currency,
                status
        );
    }

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    private static String validateOwnerSubject(String ownerSubject) {
        if (ownerSubject == null || ownerSubject.isBlank() || ownerSubject.length() > 255) {
            throw new IllegalArgumentException("Account owner is required");
        }
        return ownerSubject;
    }

    private static String validateAccountNumber(String accountNumber) {
        if (accountNumber == null || !ACCOUNT_NUMBER_PATTERN.matcher(accountNumber).matches()) {
            throw new IllegalArgumentException(
                    "Account number must contain 8 to 34 uppercase letters or digits"
            );
        }
        return accountNumber;
    }

    private static String validateCurrency(String currencyCode) {
        if (currencyCode == null || !currencyCode.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("Currency must be an uppercase ISO 4217 code");
        }
        try {
            Currency.getInstance(currencyCode);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Currency must be an uppercase ISO 4217 code");
        }
        return currencyCode;
    }

    public UUID getId() {
        return id;
    }

    public String getOwnerSubject() {
        return ownerSubject;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
