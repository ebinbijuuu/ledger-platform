# Ledger Platform — Architecture

## 1. Overview

Ledger Platform is a full-stack simulated digital banking platform designed to demonstrate modern software engineering practices, secure financial transaction processing, double-entry accounting, fraud detection, auditing, and collaborative development.

The system is built as a modular monolith with a separate frontend and backend.

## 2. High-Level Architecture

```text
┌──────────────────────────┐
│       User / Browser     │
└────────────┬─────────────┘
             │
             │ HTTPS / REST API
             ▼
┌──────────────────────────┐
│      Next.js Frontend    │
│                          │
│ React + TypeScript       │
│ Tailwind CSS             │
└────────────┬─────────────┘
             │
             │ REST / JSON
             ▼
┌──────────────────────────┐
│     Spring Boot API      │
│                          │
│ Authentication           │
│ Users                    │
│ Accounts                 │
│ Transactions             │
│ Ledger                   │
│ Fraud Detection          │
│ Audit                    │
│ Notifications            │
└────────────┬─────────────┘
             │
             │ JPA / SQL
             ▼
┌──────────────────────────┐
│       PostgreSQL         │
│                          │
│ Users                    │
│ Accounts                 │
│ Transactions             │
│ Ledger Entries           │
│ Fraud Data               │
│ Audit Logs               │
└──────────────────────────┘
```

## 3. Frontend

The frontend is built using:

- Next.js
- React
- TypeScript
- Tailwind CSS

The frontend is responsible for:

- User interface
- Authentication flows
- Account dashboards
- Transaction interfaces
- Transaction history
- Fraud alerts
- Administrative investigation interfaces
- Form validation
- Communicating with the backend API

The frontend should not contain financial business logic that needs to be trusted. Financial calculations and transaction validation are handled by the backend.

## 4. Backend

The backend is built using:

- Java
- Spring Boot
- Spring Data JPA
- Spring Security
- Maven

The backend exposes a versioned REST API and contains the application's core business logic.

Initial backend modules are:

```text
Auth
Users
Accounts
Transactions
Ledger
Fraud
Audit
Notifications
```

The backend is structured as a modular monolith rather than separate microservices.

This keeps the system easier to develop and deploy while still allowing clear separation of responsibilities.

## 5. Database

PostgreSQL is the primary source of truth for application and financial data.

Financial values will use:

- Java `BigDecimal`
- PostgreSQL `NUMERIC`

Floating-point types will not be used for monetary values.

Database migrations will be managed using Flyway.

## 6. Financial Architecture

Financial transactions will use double-entry accounting.

Every completed financial transaction must have balanced ledger entries:

```text
Total Debits = Total Credits
```

For example, a £100 transfer from Account A to Account B would produce:

```text
Account A
Debit/Credit: £100

Account B
Debit/Credit: £100
```

The exact ledger representation will be defined in the database documentation.

Financial operations must be atomic. A transaction must either complete successfully or have no financial effect.

## 7. Fraud Detection

Transactions may be assessed by a fraud detection component.

Initial risk factors may include:

- Unusually large transactions
- Unusual transaction times
- New beneficiaries
- Rapid successive transactions
- Unusual transaction amounts
- Other configurable rules

The fraud system should provide explainable reasons for a risk score rather than simply producing an unexplained result.

## 8. Audit Logging

Important system actions will be recorded in audit logs.

Audit information may include:

- User
- Action
- Resource
- Timestamp
- Relevant metadata

Audit logs are intended to provide traceability for important operations.

## 9. Security

Security responsibilities will primarily be handled by Spring Security and the backend.

Security considerations include:

- Authentication
- Authorisation
- Password security
- Input validation
- Access control
- Secure API endpoints
- Protection against duplicate financial requests
- Audit logging

Sensitive configuration such as database passwords and secret keys must not be committed to Git.

## 10. Future Infrastructure

Redis may be introduced later if a concrete requirement is identified, such as:

- Caching
- Rate limiting
- Temporary data
- Distributed locking

Redis will not be added simply for the sake of adding another technology.

## 11. Architecture Principles

The project will follow these principles:

1. Keep financial logic on the backend.
2. Treat PostgreSQL as the financial source of truth.
3. Use explicit transactions for financial operations.
4. Maintain double-entry ledger integrity.
5. Prefer simple architecture over unnecessary complexity.
6. Keep modules clearly separated.
7. Validate input at system boundaries.
8. Write automated tests for critical financial logic.
9. Never commit secrets.
10. Document significant architectural decisions.