# Ledger Platform — API

## 1. Overview

Ledger Platform exposes a RESTful API through the Spring Boot backend.

The frontend communicates with the backend using HTTP requests and JSON.

## 2. Base URL

During local development:

```text
http://localhost:8080
```

All application endpoints use the `/api/v1` prefix.

Example:

```text
GET /api/v1/health
```

## 3. API Principles

The API will follow these conventions:

- RESTful resource naming
- JSON request and response bodies
- HTTP status codes
- Versioned endpoints
- Consistent error responses
- Server-side validation
- Authentication for protected endpoints

## 4. HTTP Methods

The API will use standard HTTP methods:

| Method | Purpose |
|---|---|
| GET | Retrieve data |
| POST | Create a resource or perform an operation |
| PUT | Replace/update a resource |
| PATCH | Partially update a resource |
| DELETE | Remove a resource where appropriate |

## 5. Health Endpoint

The initial API will expose:

```text
GET /api/v1/health
```

Example response:

```json
{
  "status": "UP"
}
```

## 6. Response Format

Successful responses should return JSON appropriate to the endpoint.

Example:

```json
{
  "id": "123",
  "status": "ACTIVE"
}
```

The exact response structures will be defined as individual endpoints are implemented.

## 7. Error Responses

All API errors use the following JSON structure:

Example:

```json
{
  "timestamp": "2026-01-01T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Request validation failed",
  "path": "/api/v1/accounts",
  "fieldErrors": {
    "name": "must not be blank"
  }
}
```

`timestamp` is an ISO-8601 UTC instant. `status` and `error` contain the HTTP
status code and reason phrase. `message` is a client-safe summary, `path` is
the request path, and `fieldErrors` maps invalid field names to validation
messages. `fieldErrors` is an empty object when an error does not concern
individual fields.

The backend returns `400 Bad Request` for bean-validation failures and missing
or malformed request bodies, `404 Not Found` for a missing resource,
`409 Conflict` for a resource or operation conflict, and `500 Internal Server
Error` for unexpected failures. Expected application errors should use the
`ApiException` hierarchy (`BadRequestException`, `ResourceNotFoundException`,
or `ConflictException`) with a message that is safe to show to clients.

Error responses never include stack traces or exception and SQL details.
Unexpected errors use a fixed generic message, and internal exception messages
must not be returned to API clients.

## 8. Validation

Input received by the API must be validated before business logic is executed.

Validation responsibilities include:

- Required fields
- Field formats
- Value ranges
- Monetary amounts
- Account identifiers
- Transaction constraints

## 9. Authentication

Protected API endpoints will require authentication.

Authentication and authorisation will be implemented using Spring Security.

The final authentication mechanism will be documented when implemented.

## 10. Financial Operations

Financial endpoints must follow additional rules:

- Monetary amounts use decimal precision
- Requests are validated
- Operations are atomic
- Ledger entries must remain balanced
- Duplicate requests must be prevented where necessary
- Appropriate audit information must be recorded

## 11. Idempotency

Financial operations such as transfers should support idempotency.

An idempotency key can be used to prevent the same request from creating multiple financial transactions.

Example:

```text
Idempotency-Key: 8b7d3c2a-example
```

The exact implementation will be defined when transfer functionality is developed.

## 12. Pagination

Endpoints returning potentially large collections should support pagination.

Example:

```text
GET /api/v1/transactions?page=0&size=20
```

The final pagination response structure will be defined during implementation.

## 13. API Documentation

As the API grows, endpoint documentation will be maintained using OpenAPI/Swagger.

Each endpoint should document:

- HTTP method
- URL
- Authentication requirements
- Request parameters
- Request body
- Response body
- HTTP status codes
- Error cases
