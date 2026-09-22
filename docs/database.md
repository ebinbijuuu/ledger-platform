# Ledger Platform — Database

## 1. Database Technology

Ledger Platform uses PostgreSQL as its primary relational database.

Database schema changes are managed using Flyway migrations.

## 2. Database Principles

The database should provide:

- Referential integrity
- Appropriate constraints
- Appropriate indexes
- Transactional consistency
- Accurate monetary values
- Clear relationships between entities

## 3. Monetary Values

Monetary values must not use floating-point types.

Java will use:

```text
BigDecimal
```

PostgreSQL will use:

```text
NUMERIC
```

This prevents floating-point precision problems when handling financial values.

## 4. Initial Domain Entities

The initial domain model contains the following entities:

### User

Represents a registered user.

Potential fields:

```text
id
email
password_hash
first_name
last_name
created_at
updated_at
```

### Account

Represents a financial account belonging to a user.

Potential fields:

```text
id
user_id
account_number
account_type
currency
status
created_at
updated_at
```

### Transaction

Represents a financial transaction initiated by the system or a user.

Potential fields:

```text
id
reference
source_account_id
destination_account_id
amount
currency
status
created_at
completed_at
```

### Ledger Entry

Represents an individual debit or credit within the double-entry ledger.

Potential fields:

```text
id
transaction_id
account_id
entry_type
amount
created_at
```

### Beneficiary

Represents another account that a user can send money to.

Potential fields:

```text
id
user_id
account_id
name
created_at
```

### Fraud Assessment

Represents the fraud risk assessment associated with a transaction.

Potential fields:

```text
id
transaction_id
risk_score
risk_level
reasons
created_at
```

### Fraud Case

Represents an investigation associated with a potentially fraudulent transaction.

Potential fields:

```text
id
transaction_id
status
assigned_to
created_at
updated_at
```

### Audit Log

Records important actions performed within the system.

Potential fields:

```text
id
user_id
action
resource_type
resource_id
metadata
created_at
```

### Notification

Represents a notification sent to a user.

Potential fields:

```text
id
user_id
type
title
message
read
created_at
```

## 5. Relationships

Initial relationships include:

```text
User
 ├── Account
 ├── Beneficiary
 ├── AuditLog
 └── Notification

Account
 └── LedgerEntry

Transaction
 ├── LedgerEntry
 └── FraudAssessment

FraudAssessment
 └── FraudCase
```

## 6. Double-Entry Ledger

Every completed financial transaction must produce balanced ledger entries.

The fundamental invariant is:

```text
SUM(debits) = SUM(credits)
```

A transaction must never leave the ledger in an unbalanced state.

Financial operations must be executed atomically.

## 7. Database Constraints

Important constraints will include:

- Foreign key constraints
- Unique constraints
- Not-null constraints
- Positive monetary amounts where appropriate
- Valid transaction statuses
- Valid account statuses

Additional constraints will be added as the domain is implemented.

## 8. Indexing

Indexes will be added where they provide meaningful query performance improvements.

Potential indexes include:

- User email
- Account number
- Transaction reference
- Transaction account IDs
- Transaction timestamps
- Fraud case status
- Audit log timestamps

Indexes will be reviewed as the application's query patterns become clearer.

## 9. Database Migrations

All schema changes will be made through Flyway migrations.

Example:

```text
V1__initial_schema.sql
V2__add_beneficiaries.sql
V3__add_fraud_assessments.sql
```

Existing migrations should not be modified after they have been applied to shared environments. New changes should be introduced through new migration files.

## 10. Data Integrity

Financial data has a higher integrity requirement than ordinary application data.

The application must ensure:

- Transactions are atomic
- Ledger entries remain balanced
- Invalid account relationships cannot be created
- Duplicate financial operations are prevented
- Financial records are not silently overwritten