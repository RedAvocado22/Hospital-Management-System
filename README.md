# 🏥 Hospital Management System

An outpatient hospital management system built for the SWD392 course.

## The Problem

Outpatient hospitals deal with a lot of moving parts — receptionists managing patient queues, doctors writing medical records and prescriptions, pharmacists tracking medicine stock, cashiers handling invoices — and in many clinics this is still done on paper or disconnected spreadsheets. Patients have no way to book appointments online or view their own records.

This system puts all of that into one platform: patient check-in → doctor examination → prescription → billing, with every role seeing only what they need.

## What we're practicing here

- **Keycloak** for auth instead of hand-rolling JWT — OAuth2 / OpenID Connect with role-based access
- **Flyway** for versioned database migrations — no more `ddl-auto: update`
- **Redis** for caching and session management
- **Docker Compose** to spin up the full infra stack in one command
- **Modular package structure** with a unified `BaseService` pipeline pattern
- **Standardized API responses** via `ApiResponse<T>` wrapper across all endpoints

## Tech Stack

| Layer | Stack |
|---|---|
| **API** | Java 17, Spring Boot, Spring Security, Spring Data JPA |
| **Database** | MySQL 8.0, Flyway |
| **Cache** | Redis 7.4 |
| **Auth** | Keycloak 26 (OAuth2 / JWT) |
| **Frontend** | React 19, TypeScript, Vite, Ant Design 6 |
| **State** | Zustand, React Query, Zod |

## Project Structure

```
├── hms-api/                     # Spring Boot backend
│   ├── src/.../hms/
│   │   ├── auth/                # Accounts, roles, employees
│   │   ├── patient/             # Patient info, insurance
│   │   ├── medical/             # Records, doctor schedules
│   │   ├── pharmacy/            # Medicines, prescriptions
│   │   ├── billing/             # Invoices, payments
│   │   ├── appointment/         # Booking
│   │   ├── notification/        # Reminders
│   │   ├── base/                # BaseService, ApiResponse, DTOs
│   │   └── exception/           # Global error handling
│   ├── resources/db/migration/  # Flyway SQL
│   └── compose.yaml             # Docker infra
│
├── hms-ui/                      # React frontend (Vite)
└── docs/                        # Design docs (gitignored)
```

## Setup

**Prerequisites:** Java 17+, Maven 3.9+, Node 20+, Docker

```bash
# 1. env
cp .env.example .env         # fill in your credentials

# 2. infra
cd hms-api
docker compose up -d          # MySQL :3306, Redis :6379, Keycloak :9090

# 3. backend
mvn spring-boot:run           # API → localhost:8080/api
                              # Swagger → localhost:8080/api/swagger-ui.html

# 4. frontend
cd ../hms-ui
npm install && npm run dev    # UI → localhost:5173
```

## Roles

| Role | What they do |
|---|---|
| **Admin** | Employee & department management |
| **Doctor** | Medical records, prescriptions, OPD appointments |
| **Reception** | Patient check-in, exam queue, insurance validation |
| **Pharmacist** | Medicine inventory |
| **Cashier** | Billing & payments |
| **Patient** | Book appointments, view records |

## Environment Variables

All variables are listed in [`.env.example`](.env.example). The app won't start without them — no hardcoded defaults for credentials.
