# National Bank Backend

Spring Boot backend for the National Bank React UI.

## Stack

- Java 17
- Spring Boot 3.3
- Spring Security + JWT
- PostgreSQL
- Flyway
- Docker Compose (PostgreSQL + pgAdmin)

## Prerequisites

- Java 17+
- Maven 3.9+ (or use the included Maven wrapper if present)
- Docker Desktop (or Docker Engine + Docker Compose plugin)
- Node.js 18+ (only if running the frontend locally)

## Quick start

### 1. Clone and enter the project

```bash
git clone <repository-url>
cd banking-backend
```

### 2. Start database services (Docker)

From the project root:

```bash
docker compose up -d
```

This starts PostgreSQL and pgAdmin in the background. Wait until both containers are healthy:

```bash
docker compose ps
```

### 3. Run the backend

```bash
mvn spring-boot:run
```

Backend runs at **http://localhost:8080**

Swagger UI: **http://localhost:8080/swagger-ui.html**

### 4. Run the frontend (optional)

If you have the companion React UI in a sibling folder:

```bash
cd ../Banking
npm install
npm run dev
```

Frontend runs at **http://localhost:3000** and proxies `/users` and `/api` to the backend.

## Docker services

| Service    | Host URL                  | Container name      | Credentials |
|------------|---------------------------|---------------------|-------------|
| PostgreSQL | `localhost:5432`          | `banking-postgres`  | DB: `banking_db`, user: `banking_user`, password: `banking_pass` |
| pgAdmin    | http://localhost:5050     | `banking-pgadmin`   | email: `admin@bank.com`, password: `admin123` |

### Access pgAdmin in the browser

pgAdmin is the web UI for managing the PostgreSQL database running in Docker.

1. Make sure Docker services are running:

   ```bash
   docker compose up -d
   ```

2. Open pgAdmin in your browser:

   **http://localhost:5050**

3. Sign in with:

   - **Email:** `admin@bank.com`
   - **Password:** `admin123`

4. Register the PostgreSQL server (first time only):

   - Click **Add New Server**
   - **General** tab → Name: `Banking DB` (any name is fine)
   - **Connection** tab:
     - **Host name/address:** `postgres` (Docker service name — use this when pgAdmin runs inside Docker)
     - **Port:** `5432`
     - **Maintenance database:** `banking_db`
     - **Username:** `banking_user`
     - **Password:** `banking_pass`
   - Save the password if prompted, then click **Save**

5. Browse the database:

   - Expand **Servers → Banking DB → Databases → banking_db → Schemas → public → Tables**
   - You should see tables such as `users`, `transactions`, `expenses`, `debts`, and `budgets`

> **Note:** If you connect to PostgreSQL from a tool running on your host machine (outside Docker), use `localhost` as the host instead of `postgres`.

### Useful Docker commands

```bash
# Start services
docker compose up -d

# Stop services
docker compose down

# View logs
docker compose logs -f

# Stop and remove volumes (resets database data)
docker compose down -v
```

## Seed users

| Email            | Password   | Role  |
|------------------|------------|-------|
| `admin@bank.com` | `admin123` | ADMIN |
| `user@bank.com`  | `user123`  | USER  |

The demo user also has sample transactions, expenses, and debts.

## API overview

### Public
- `POST /users/register`
- `POST /users/login`

### Protected (Bearer JWT)
- Transactions: `/api/transaction/*`
- Expenses: `/api/{userId}/expense/*`
- Debts: `/api/debts/*`
- Budgets: `/api/budget/*`

## Postman collection

Import the file **`National-Bank-Backend.postman_collection.json`** into Postman to test all endpoints.

1. Open Postman → **Import** → select the JSON file from this repo
2. Run **Auth → Login** first (saves the JWT token and user ID automatically)
3. Run the remaining requests in any order

Collection variables (editable in Postman):

| Variable   | Default                 | Description              |
|------------|-------------------------|--------------------------|
| `baseUrl`  | `http://localhost:8080` | Backend base URL         |
| `token`    | (auto-set on login)     | JWT from login response  |
| `userId`   | (auto-set on login)     | Logged-in user ID        |
| `budgetId` | `1`                     | Budget ID for sub-routes |

## Authorization

- `ROLE_USER`: can access only their own records
- `ROLE_ADMIN`: can access all users' records

## Example curl

```bash
curl -X POST http://localhost:8080/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@bank.com","password":"user123"}'
```

```bash
curl http://localhost:8080/api/transaction/balance/2 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

## Tests

```bash
mvn test
```
