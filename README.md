# NannyHub · Backend

[Русская версия](README.ru.md) · [Frontend](https://github.com/wnxsts/nannyhub-frontend)

NannyHub connects parents looking for childcare with nannies. Parents browse profiles and send likes. Nannies can return a like; mutual interest creates a match and a conversation.

This repository holds the Spring Boot API, authentication, matching logic, message storage and database migrations. The React frontend is deployed separately.

## What is implemented

- Parent and nanny registration, BCrypt password hashing and JWT sign-in.
- Role-specific profiles and a current-user endpoint.
- Nanny discovery, incoming likes and mutual matches.
- Chat history and sending messages through REST, plus STOMP/SockJS endpoints.
- PostgreSQL schema managed by Liquibase.

This is an MVP, without payments, booking or identity verification. **Chat access control is incomplete:** REST chat endpoints do not check match membership, and STOMP messages accept a client-supplied sender ID. Do not use the current version for private conversations on a public shared instance.

## Stack

Java 21, Spring Boot 4.0.1, Spring Security, Spring Data JPA, PostgreSQL, Liquibase, Maven and JJWT. OpenAPI integration is present; verify Swagger UI compatibility when upgrading Spring Boot or springdoc.

## Run locally

Install Java 21 and create a PostgreSQL database named `nanny_hub_db`. The Maven wrapper is included.

```sh
cp .env.example .env
# Edit .env: set your database password and a random JWT secret.
set -a
. ./.env
set +a
./mvnw spring-boot:run
```

Spring Boot does not automatically load `.env`; the shell commands above export it. Generate a JWT secret with `openssl rand -hex 32`. Keep `.env` out of Git.

| Variable | Purpose |
| --- | --- |
| `DATABASE_URL` | JDBC URL; defaults to `jdbc:postgresql://localhost:5432/nanny_hub_db` |
| `DATABASE_USERNAME` | Database user; defaults to `postgres` |
| `DATABASE_PASSWORD` | Required database password |
| `JWT_SECRET` | Required signing secret, at least 32 bytes |
| `ALLOWED_ORIGINS` | Comma-separated exact frontend origins; defaults to `http://localhost:5173` |
| `PORT` | HTTP port; defaults to `8080` |

Liquibase applies migrations on startup; Hibernate validates the resulting schema.

```sh
./mvnw test                  # Requires the configured PostgreSQL database
./mvnw -DskipTests package   # Build the executable JAR without running tests
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

## API areas

| Path | Responsibility |
| --- | --- |
| `/api/auth/**` | Registration and sign-in |
| `/api/me` | Current user |
| `/api/parent/**`, `/api/nanny/**` | Profiles |
| `/api/swipes/**` | Discovery and likes |
| `/api/matches/**` | Matches |
| `/api/chats/**` | Chat messages and history |
| `/ws` | SockJS/STOMP connection |
| `/actuator/health` | Health probe |

Protected HTTP requests use `Authorization: Bearer <token>`.

## Deploy separately

The included Dockerfile builds with Java 21 and runs the executable JAR as a non-root user:

```sh
docker build -t nannyhub-backend .
docker run --rm --env-file .env -p 8080:8080 nannyhub-backend
```

For a container, `DATABASE_URL` must address a reachable database host, not the container's own `localhost`. Use a separate PostgreSQL service, configure its credentials through the hosting platform, and use the provider's TLS settings. Set `ALLOWED_ORIGINS` to the deployed frontend's exact HTTPS origin. Use `/actuator/health` for health checks and enable WebSocket forwarding on the reverse proxy.

The frontend needs this service's HTTPS origin in `VITE_API_URL` at build time. No hosting account or live deployment URL is configured here. Resolve the chat access-control limitation above before a public release.

## Code map

`controller` exposes endpoints; `service` holds application logic; `repository` and `entity` handle persistence; `security` handles JWT authentication. Database migrations live in `src/main/resources/db/changelog`.
