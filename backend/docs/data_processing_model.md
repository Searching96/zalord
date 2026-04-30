# Data Processing Model

This project follows a consistent, layered data flow to keep domain logic, persistence, and HTTP concerns cleanly separated.

## Core Terms

- Request: Input DTO received by an endpoint.
- Result: Use case output DTO (business-level result).
- View: Repository projection (partial read model) to minimize DB I/O.
- Response: Output DTO returned to the client.
- JPQL: Used for projection queries when only a subset of columns is needed.
- Exception Handler: Endpoint-level mapping from domain exceptions to HTTP status codes.

## Standard Flow

Request -> UseCase -> Result -> Response
              ^
              |
           Repository (View via JPQL)

## Responsibilities

1) Endpoint
- Accepts Request.
- Calls UseCase.
- Maps Result to Response.
- Handles domain exceptions with @ExceptionHandler and returns proper HTTP status.

2) Use Case
- Executes business logic and validation.
- Pulls data through repositories using View projections (not entities when only partial data is needed).
- Throws domain-specific exceptions for error cases.

3) Repository
- Exposes View projections for read use cases.
- Uses JPQL to fetch only required fields when needed.
- Keeps entity loading for write paths.

## When to Use JPQL and Views

- Use View + JPQL for read-only paths that only need a subset of fields.
- Use entities for writes or when full object state is required.

## Exception Handling Pattern

- Use cases throw domain-specific RuntimeExceptions.
- Endpoints declare @ExceptionHandler methods that map exceptions to HTTP status codes.
- Avoid try/catch in endpoints for normal control flow.

## Example Mapping Patterns

- Request -> UseCase input parameters.
- View -> Result (UseCase constructs Result from View).
- Result -> Response (Endpoint constructs Response from Result).
