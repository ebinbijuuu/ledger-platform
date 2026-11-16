CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    owner_subject VARCHAR(255) NOT NULL,
    account_number VARCHAR(34) NOT NULL UNIQUE,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(16) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT chk_accounts_owner_subject_not_blank
        CHECK (length(trim(owner_subject)) > 0),
    CONSTRAINT chk_accounts_number_length
        CHECK (length(account_number) BETWEEN 8 AND 34),
    CONSTRAINT chk_accounts_number_uppercase
        CHECK (account_number = upper(account_number)),
    CONSTRAINT chk_accounts_currency_length
        CHECK (length(currency) = 3 AND currency = upper(currency)),
    CONSTRAINT chk_accounts_status
        CHECK (status IN ('ACTIVE', 'FROZEN', 'CLOSED'))
);

CREATE INDEX idx_accounts_owner_subject
    ON accounts (owner_subject);
