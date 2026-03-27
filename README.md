# Hospital Management System

## Overview

A production-grade hospital management REST API built as a course project (SWD392) simulating real-world clinical workflows — patient registration, appointment scheduling, medical records, pharmacy, and billing. The backend is a stateless JWT-secured Spring Boot service integrated with Keycloak for identity and Redis for concurrent slot management. The frontend is a React 19 + TypeScript SPA. The project is a two-person team effort with clear domain ownership and a shared base infrastructure.

---

## Tech Stack

| Layer        | Technology                                              |
|--------------|---------------------------------------------------------|
| Backend      | Spring Boot 3.4.3 (Java 17), Spring Data JPA, Hibernate |
| Auth         | Keycloak 26.1 (JWT OAuth2 Resource Server)              |
| Database     | MySQL 8.0, Flyway migrations                            |
| Cache        | Redis 7.4 (appointment slots, JWT blacklist)            |
| Mapping      | MapStruct 1.6.3                                         |
| API Docs     | SpringDoc OpenAPI 2.8.4 (Swagger UI)                    |
| Export       | OpenPDF, Apache POI                                     |
| Frontend     | React 19, TypeScript, Vite                              |
| Infra        | Docker Compose                                          |

---

## Architecture

```
Controller → Service → Repository → Entity (JPA) → MySQL
```

All services implement a **template method pipeline**: `execute() → validate() → doProcess()`. The `execute()` method is the transaction boundary and the only public entry point — calling `doProcess()` directly bypasses validation and Spring's AOP proxy (which would silently break `@Transactional`).

Every endpoint returns a uniform `ApiResponse<T>`. Every entity extends `BaseEntity` (UUID PK, `createdAt`, `updatedAt`). Roles arrive in the JWT under `realm_access.roles`, extracted and prefixed with `ROLE_` by `KeycloakRoleConverter`.

Infrastructure boundaries are strict: all Keycloak calls go through `KeycloakService`, all Redis slot operations through `AppointmentSlotService`, all JWT blacklisting through `TokenBlacklistService`, all notifications through `NotificationDispatcher`. No other class touches these systems directly.

---

## Running Locally

```bash
# 1. Start infrastructure (MySQL, Redis, Keycloak)
cd hms-api
docker-compose up -d

# 2. Start the API
./mvnw spring-boot:run
```

| Service    | URL                                       |
|------------|-------------------------------------------|
| API        | http://localhost:8080/api                 |
| Swagger UI | http://localhost:8080/api/swagger-ui.html |
| Keycloak   | http://localhost:9090                     |
| Frontend   | http://localhost:5173                     |

---

## Contributors

### Nguyen Minh Cuong — Backend & Frontend

**Modules:** Base infrastructure, Auth, Employee, Department, Medical Records, Frontend (Employee pages, Medical Records pages, JWT utils, role constants)

- **BaseService template method** — Designed the `execute() → validate() → doProcess()` pipeline shared by all services. Subclasses override `execute()` (not `doProcess()`) to place `@Transactional` on the proxy-visible method — avoiding the Spring AOP self-invocation trap where `@Transactional` on an internal method is silently skipped by the JVM proxy.

- **JPA Specification + EntityGraph** — Dynamic multi-predicate filtering (10+ optional fields) via the JPA Criteria API. `@EntityGraph` on individual repository methods specifies exact joins per query — eliminates N+1 without global `FetchType.EAGER`, which would over-fetch on every query path.

- **MySQL full-text search** — Native `MATCH...AGAINST (IN BOOLEAN MODE)` query for patient search by name/phone across joined tables. Custom Flyway V8 migration adds the FULLTEXT index. A separate `countQuery` is explicitly provided so Spring Data can paginate correctly — without it, the ORM can't derive the count from a complex native join.

- **Attribute-Based Access Control (ABAC)** — Doctors see only their own medical records. The filter is injected at the SQL level (`:doctorId IS NULL OR mr.doctor_id = :doctorId`) rather than applied post-fetch. Post-fetch filtering corrupts paginated total counts — the predicate belongs in the query, not in application code.

- **Two-phase sync with compensation** — Account creation: MySQL insert first, then Keycloak registration. On Keycloak failure: `keycloakService.deleteUser()` (compensation) + rethrow → `@Transactional` rolls back MySQL. Prevents ghost users where MySQL has a row but Keycloak has no corresponding identity.

- **Transactional composition** — `POST /medical-records` atomically creates three DB records (MedicalRecord + ServiceInvoice + MedicineInvoice) in one `@Transactional`. Uses `getReferenceById()` for FK assignments to skip redundant SELECT queries — the proxy is sufficient; the full entity is never needed.

- **Domain boundary services** — Each domain exposes a `QueryService` (plain `@Service`, returns DTOs) as its public API. Other domains inject this instead of importing foreign repositories or entities directly. Keeps coupling explicit, boundaries clear, and each domain independently swappable.

- **`open-in-view: false`** — Disabled Spring's default open-session-in-view. Session closes after the repository call, not after the HTTP response is written. Forces all lazy association access into explicit `@Transactional` scopes — prevents the classic silent lazy query during JSON serialization that degrades production throughput.

---

### [Person 2 Name] — Backend

**Modules:** Patient registration, Pharmacy (medicine CRUD, stock management, full-text search), Doctor Schedule management, Medical Exam Queue / Receptionist flow, Insurance validation

*(Technical highlights — to be filled in)*
