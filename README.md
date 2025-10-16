# Smart Interview Platform

Documentation and setup instructions for the Smart Interview Platform backend.

This Spring Boot application provides a backend for an interview practice platform with users, questions, code submissions, and leaderboards.

Highlights
- Spring Boot (3.5.x)
- Spring Security with JWT authentication
- Spring Data JPA + MySQL
- OpenAPI (springdoc) for documentation

Contents
- Overview
- Requirements
- Setup & run
- Database
- Authentication
- API endpoints (summary + quick list)
- Project structure
- Notes & caveats

Overview
--------
This repository implements a basic interview-prep platform backend. Main features:

- User registration and authentication (JWT)
- CRUD for programming questions (admin)
- Code submissions tied to users and questions
- Per-user statistics and leaderboards

Requirements
------------
- Java 24 (project is configured to use java.version=24 in pom.xml)
- Maven
- MySQL running locally (or change datasource to your DB)

Setup & run
-----------
1. Clone the repository.
2. Create a MySQL database (default name used in `application.properties` is `smart_prep_db`).
3. Update `src/main/resources/application.properties` with your DB credentials if different.
4. Build and run with Maven:

```powershell
mvnw.cmd clean package
mvnw.cmd spring-boot:run
```

The app runs on port defined in `application.properties` (default is 8181). So the base URL is:

- http://localhost:8181

Database
--------
Default settings (see `application.properties`):

- URL: jdbc:mysql://localhost:3306/smart_prep_db
- username: root
- password: 7070
- Hibernate ddl-auto: update (will update schema automatically)

Authentication
--------------
The app uses JWT tokens. Workflow:

1. Register a user with POST /auth/register. New users are created with role ROLE_USER by default.
2. Login with POST /auth/login (email and password). The response is a JWT token string.
3. Send protected requests with header:

   Authorization: Bearer <token>

Security rules (from `Configuration/SecurityConfig.java`)

- /auth/** — public
- /submission/**, /user/**, /leaderboard/** — require ROLE_USER or ROLE_ADMIN
- Some question operations use method-level `@PreAuthorize`:
  - POST /questions/add, PUT /questions/question/{id}, DELETE /questions/question/{id} — ADMIN only
  - Question read/search endpoints — USER or ADMIN

API Endpoints (summary)
------------------------
Base URL: http://localhost:8181

General
- GET  / — Public — Health/status string

Authentication (public)
- POST /auth/register — Register a new user (User JSON)
- POST /auth/login — Login (User JSON with email, password). Returns JWT token.

Users
- POST /user/users — Protected (USER|ADMIN) — body: { "email": "..." }
- GET  /user/all — Protected (USER|ADMIN)
- GET  /user/id/{id} — Protected (USER|ADMIN)
- GET  /user/email/{email} — Protected (USER|ADMIN)

Questions
- POST /questions/add — ADMIN only
- GET  /questions/all — Protected (USER|ADMIN)
- GET  /questions/question/{id} — Protected (USER|ADMIN)
- PUT  /questions/question/{id} — ADMIN only
- DELETE /questions/question/{id} — ADMIN only
- GET  /questions/search?keyword={keyword} — Protected (USER|ADMIN)
- GET  /questions/searchByTag?tag={tag} — Protected (USER|ADMIN)

Code Submissions
- POST /submission/submit — Protected (USER|ADMIN)
- GET  /submission/user/{userId} — Protected (USER|ADMIN)
- GET  /submission/question/{questionId} — Protected (USER|ADMIN)
- GET  /submission/submission/{submissionId} — Protected (USER|ADMIN)
- PUT  /submission/{submissionId} — Protected (USER|ADMIN)
- DELETE /submission/{submissionId} — Protected (USER|ADMIN)

Leaderboard
- GET /leaderboard/{leaderBoardType} — Protected (USER|ADMIN)

Endpoint Quick List (compact)
-----------------------------
- GET  /                                  -> Public
- POST /auth/register                      -> Public
- POST /auth/login                         -> Public (returns JWT)

- POST /user/users                         -> Protected (USER|ADMIN)
- GET  /user/all                           -> Protected (USER|ADMIN)
- GET  /user/id/{id}                       -> Protected (USER|ADMIN)
- GET  /user/email/{email}                 -> Protected (USER|ADMIN)

- POST /questions/add                      -> ADMIN only
- GET  /questions/all                      -> Protected (USER|ADMIN)
- GET  /questions/question/{id}            -> Protected (USER|ADMIN)
- PUT  /questions/question/{id}            -> ADMIN only
- DELETE /questions/question/{id}         -> ADMIN only
- GET  /questions/search?keyword={k}       -> Protected (USER|ADMIN)
- GET  /questions/searchByTag?tag={t}      -> Protected (USER|ADMIN)

- POST /submission/submit                  -> Protected (USER|ADMIN)
- GET  /submission/user/{userId}           -> Protected (USER|ADMIN)
- GET  /submission/question/{questionId}   -> Protected (USER|ADMIN)
- GET  /submission/submission/{submissionId} -> Protected (USER|ADMIN)
- PUT  /submission/{submissionId}          -> Protected (USER|ADMIN)
- DELETE /submission/{submissionId}        -> Protected (USER|ADMIN)

- GET  /leaderboard/{leaderBoardType}      -> Protected (USER|ADMIN)

Project structure (important folders)
- src/main/java/com/smartprep/smart_interview_platform/
  - Controller/      (REST controllers)
  - Service/         (business logic)
  - Repository/      (Spring Data JPA interfaces)
  - Model/           (JPA entities)
  - Configuration/   (Security, Swagger, app beans)
  - Security/        (JWT utility & filter)

Notes & caveats
----------------
- Method-level `@PreAuthorize` checks are used in controllers. Ensure method security is enabled if you rely on those annotations (this project uses path-based rules in `SecurityConfig` and also registers `JwtAuthenticationFilter`). If you need, I can enable method security explicitly.
- Passwords are stored encoded using BCrypt via `PasswordEncoder` bean.
- The JWT secret is hard-coded in `JwtUtil` (for development). For production, move it to secure config (environment variables or a secrets manager).
- The application currently uses `spring.jpa.hibernate.ddl-auto=update`. Consider using migrations (Flyway/Liquibase) for production.

Contributing
------------
- Fork, create a feature branch, run tests and open a PR. Add tests for new behavior.

License
-------
This project does not declare a license in `pom.xml`. Add a LICENSE file if you plan to make it public.

---
Generated by scanning the project source. If you'd like I can:

- add example PowerShell commands demonstrating login + protected request
- create a Postman collection or OpenAPI YAML
- enable method security or tighten security defaults
