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

### Prerequisites

- Java 17+
- Docker Desktop (must be running)

### Start

Just run the Spring Boot app from IntelliJ (or `./mvnw spring-boot:run` inside `hms-api/`).

Spring Boot automatically starts all Docker containers (MySQL, Redis, Keycloak), waits for them to be healthy, then boots the API. No manual `docker compose` step needed.

| Service    | URL                                       |
|------------|-------------------------------------------|
| API        | http://localhost:8080/api                 |
| Swagger UI | http://localhost:8080/api/swagger-ui.html |
| Keycloak   | http://localhost:9090                     |
| Frontend   | http://localhost:5173                     |

### Default Credentials

**Keycloak Admin:** `admin / admin` at `http://localhost:9090`

**Seed users** (all passwords: `123456`):

| Username | Role |
|---|---|
| `admin.seed` | ADMIN |
| `dr.nguyen` | DOCTOR |
| `dr.tran` | DOCTOR |
| `receptionist.le` | RECEPTIONIST |
| `pharmacist.pham` | PHARMACIST |
| `cashier.hoang` | CASHIER |
| `patient.bui` | PATIENT |
| `patient.vo` | PATIENT |
| `patient.dang` | PATIENT |

### Full Reset (broken DB, Keycloak out of sync, Flyway checksum errors)

```bash
cd hms-api
docker compose down -v
```

Then run the project again — schema, seed data, and Keycloak realm all rebuild from scratch automatically.

> **Warning:** `-v` deletes all volumes including the database. Only use this for a clean slate.

---

## Contributors

### Nguyen Minh Cuong — Backend & Frontend

**Modules:** Base infrastructure, Auth, Employee, Department, Medical Records, Appointment Booking, Frontend (Employee pages, Medical Records pages)

---

**Zero overbooking under concurrent load — Redis Lua atomic booking**

Appointment slots are tracked as Redis counters. The naive approach (GET → check → INCR) has a race window: two concurrent requests both read the same value, both pass the check, both book — slot exceeded. The fix is a single Lua script that Redis executes atomically. No other command can interleave. Cold-start seeds the counter from the DB count so the system stays consistent after Redis restarts or evictions.

```
slots:{doctorId}:{date}:{startTime}-{endTime}
```

---

**~90% fewer DB queries on paginated reads — N+1 eliminated via EntityGraph**

Default JPA lazy loading fires one query per associated entity. A page of 20 medical records with doctor + patient associations = 41 queries. `@EntityGraph` per repository method specifies exact joins — 2 queries total regardless of page size. No `FetchType.EAGER` (which would over-fetch on every code path).

---

**3 SELECT queries eliminated per write — JPA proxy references**

`POST /medical-records` creates 3 records atomically (MedicalRecord + ServiceInvoice + MedicineInvoice). FK assignments use `getReferenceById()` — a JPA proxy that holds the ID without hitting the DB. No SELECT before INSERT for patient, doctor, or record references.

---

**Other highlights**

- **ABAC** — Doctors filtered to own records at SQL level, not post-fetch. PATIENT booking resolves `patientId` from JWT — cannot spoof another patient's ID.
- **Full-text search** — `MATCH...AGAINST (IN BOOLEAN MODE)` on a MySQL FULLTEXT index. Avoids `LIKE '%keyword%'` which can't use indexes and degrades on large tables.
- **Two-phase Keycloak sync** — MySQL insert first, Keycloak second. On failure: compensation delete + rethrow → `@Transactional` rollback. No ghost users.
- **`open-in-view: false`** — Prevents hidden lazy queries firing during JSON serialization after the session should be closed.
- **Template method BaseService** — Shared `execute() → validate() → doProcess()` pipeline. `@Transactional` placed on `execute()`, not `doProcess()` — avoids Spring AOP self-invocation where the proxy is bypassed and the annotation silently does nothing.
- **Domain boundary query services** — No cross-domain repository imports. Each domain exposes a `QueryService` returning DTOs as its public API.

---

### [Person 2 Name] — Backend

**Modules:** Patient registration, Pharmacy (medicine CRUD, stock management, full-text search), Doctor Schedule management, Medical Exam Queue / Receptionist flow, Insurance validation

*(Technical highlights — to be filled in)*
