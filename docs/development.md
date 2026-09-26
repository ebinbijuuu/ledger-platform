# Ledger Platform — Development Setup

## 1. Prerequisites

The following software is required:

- Git
- Java 21 or later
- Node.js
- npm
- Docker
- Docker Compose

An IDE such as IntelliJ IDEA or WebStorm is recommended.

## 2. Clone the Repository

Clone the repository:

```bash
git clone <repository-url>
```

Move into the project directory:

```bash
cd ledger-platform
```

## 3. Environment Variables

Create your local environment file from the example in the repository root:

```bash
cp .env.example .env
```

The example contains the shared PostgreSQL settings used by Docker Compose and Spring Boot. You can change these values in `.env`; Spring Boot reads it automatically when you start the backend from the `backend` directory.

The committed `.env.example` contains development defaults only. Keep real credentials and local overrides in `.env`; Git ignores that file.

## 4. Start PostgreSQL

Start the development database from the repository root. Docker Compose reads `.env` automatically:

```bash
docker compose up -d
```

Verify that the PostgreSQL container is running:

```bash
docker compose ps
```

## 5. Run the Backend

Navigate to the backend:

```bash
cd backend
```

The Maven Wrapper is included, so a global Maven installation is not required.

Start the Spring Boot application using Maven. Run this from the `backend` directory so it can load `../.env`:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

The backend should be available at:

```text
http://localhost:8080
```

## 6. Verify the Backend

Test the health endpoint:

```text
GET /api/v1/health
```

Expected response:

```json
{
  "status": "UP"
}
```

## 7. Run the Frontend

Navigate to the frontend:

```bash
cd frontend
```

Install dependencies:

```bash
npm install
```

Start the development server:

```bash
npm run dev
```

The frontend should be available at:

```text
http://localhost:3000
```

## 8. Running Tests

### Frontend

Run:

```bash
npm test
```

or the project's configured test command.

### Backend

Run:

```bash
./mvnw test
```

On Windows:

```bash
mvnw.cmd test
```

## 9. Git Workflow

Development should use feature branches.

Example:

```bash
git checkout -b feature/account-management
```

Make changes, commit them, and push the branch:

```bash
git add .
git commit -m "feat: add account management"
git push -u origin feature/account-management
```

Create a pull request on GitHub.

The other developer should review the pull request before it is merged into `main`.

## 10. Pull Requests

Pull requests should:

- Have a clear title
- Describe the changes
- Reference the relevant issue
- Include appropriate tests
- Pass CI checks
- Receive at least one approval

Direct pushes to `main` are not permitted.

## 11. Commit Convention

The project will use conventional-style commit prefixes:

```text
feat:
fix:
refactor:
test:
docs:
chore:
```

Examples:

```text
feat: add account creation
fix: prevent duplicate transfers
test: add ledger balance tests
docs: update API documentation
chore: configure docker compose
```

## 12. Local Development Principles

Developers should:

- Keep branches focused on a single task
- Write tests for important functionality
- Avoid committing secrets
- Keep pull requests reasonably small
- Review each other's code
- Update documentation when behaviour changes
- Keep `main` in a working state

## 13. Continuous Integration

GitHub Actions runs CI automatically on:

- Every pull request targeting `main`
- Every push to `main`

Two checks run:

- **Backend CI** — sets up Java 21, then runs `./mvnw clean verify` from `backend/`.
- **Frontend CI** — sets up Node (version pinned in `.nvmrc`), then runs `npm ci`, `npm run lint`, and `npm run build` from `frontend/`.

Both checks are required — a pull request cannot be merged into `main` until both pass. Check the **Checks** tab on your PR to see build/test/lint output and diagnose failures.

Frontend tests will be added to this pipeline once test coverage exists; there is currently no `test` script in `frontend/package.json`.