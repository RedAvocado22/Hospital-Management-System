# HMS Backend Plan — 2 People, 6 Weeks

> Backend-first. Never start a phase until the previous one is done.

---

## Dependency Tree

```
Auth (Login / Register / Roles)
 ├── Patient / Employee exist
 │    └── Doctor Schedule exists
 │         └── Appointment / Medical Queue
 │              └── Medical Record
 │                   ├── Prescriptions → Medicine Invoice
 │                   ├── Service Invoice
 │                   └── Hospital Fee Payment
```

> ⛔ **Never start a row until everything above it is done.** If you break this rule you'll waste time building features that can't be tested because the data they depend on doesn't exist yet.

---

## Phase 1 — Foundation (Week 1, do together)

Both people do this together before splitting. Nothing else works without it.

| Order | Feature | Why first | Owner |
|---|---|---|---|
| 1 | DB schema + Redis setup | Everything depends on this | Both |
| 2 | Login (all roles) | Every use case requires login | Both |
| 3 | Register (Patient) | Patient must exist to test anything | Both |
| 4 | Role-based routing/guard | Prevents wrong roles from accessing endpoints | Both |

### Detailed tasks
- **Flyway migrations:** `V2__core_schema.sql` (all tables from the spec — roles, departments, accounts, employees, patients, insurance, medical records, medicines, invoices, payments, appointments, notifications)
- **Redis:** Docker compose setup, Spring Data Redis config, connection test
- **Keycloak:** Resource server config, JWT validation, role extraction
- **Auth APIs:** `POST /auth/login`, `POST /auth/register`, `GET /auth/me`
- **Security:** `@PreAuthorize` annotations, `SecurityConfig` refinements
- **Base infra:** Finalize `ApiResponse`, DTOs, validation framework, `GlobalExceptionHandler`

---

## Phase 2 — Core Data (Week 2, split here)

These are all **lookup/master data** — no dependencies on each other so both people can work fully in parallel.

| Person 1 | Person 2 |
|---|---|
| Add New Department | View Medicine List |
| Add New Employee | Add New Medicine |
| View Employee List | Edit Medicine |
| Update Employee | De-activate Medicine |
| Remove Employee | View Medicine Detail |
| De-activate Employee | Add New Patient |

### Person 1 — Admin domain
- **Department API:** `POST /departments`, `GET /departments`
- **Employee APIs:** `GET /employees`, `GET /employees/{id}`, `POST /employees`, `PUT /employees/{id}`, `DELETE /employees/{id}`, `PATCH /employees/{id}/deactivate`
- Role security: `@PreAuthorize("hasRole('ADMIN')")`

### Person 2 — Pharmacy + Patient domain
- **Medicine APIs:** `GET /medicines` (paginated), `GET /medicines/{id}`, `POST /medicines`, `PUT /medicines/{id}`, `PATCH /medicines/{id}/deactivate`
- **Patient API:** `POST /patients`
- Role security: `@PreAuthorize("hasRole('PHARMACIST')")` for medicines

---

## Phase 3 — Scheduling & Queue (Week 3)

Person 2 must do doctor schedule before appointments. Person 1 can start medical records (read-only).

| Person 1 | Person 2 |
|---|---|
| View Medical Record List | View All Doctor Schedule |
| View Medical Record Detail | View Medical Examination Queue |
| — | Edit/Remove Queue |
| — | Validate Health Insurance |

### Person 1
- **Medical Record read APIs:** `GET /medical-records` (searchable by patient name/phone/ID), `GET /medical-records/{id}`

### Person 2
- **Doctor Schedule API:** `GET /doctor-schedules` (filter by doctor, date — receptionist view)
- **Exam Queue APIs:** `GET /exam-queue`, `POST /exam-queue`, `PUT /exam-queue/{id}`, `DELETE /exam-queue/{id}`
- **Insurance API:** `POST /insurance/validate` (mock BHXH system call, save result)

---

## Phase 4 — Core Transactions (Week 4)

This is the most important phase. Dependencies are tight here.

| Person 1 | Person 2 |
|---|---|
| Add Medical Record | Book Medical Examination |
| Update Medical Record | View Patient Exam History |
| — | View Patient Exam Detail |

> ⚠️ Person 1's **Add Medical Record** auto-creates Service Invoice + Medicine Invoice.
> Person 2's **Book Medical Examination** needs `doctor_schedule` from Phase 3. Make sure those are done first.

### Person 1
- **Medical Record write APIs:** `POST /medical-records` (auto-create `ServiceInvoice` + `MedicineInvoice`), `PUT /medical-records/{id}`
- Business logic: validate required fields (symptoms, diagnosis), create linked invoices on save

### Person 2
- **Booking API:** `POST /appointments` (patient books), validate doctor availability from schedule
- **Patient History APIs:** `GET /patients/{id}/examination-history`, `GET /examinations/{id}`

---

## Phase 5 — Billing + Prescriptions (Week 5)

These depend on medical records existing.

| Person 1 | Person 2 |
|---|---|
| Create Medicine Prescription | Follow-up Date Reminder |
| Print Medicine Prescription | Booking confirmation notification |
| View Hospital Fee Payment List | — |
| Update Hospital Fee Payment | — |
| Print Hospital Fee Payment | — |

### Person 1
- **Prescription APIs:** `POST /prescriptions` (validate stock → update medicine quantity → create prescription items), `GET /prescriptions/{id}`, `GET /prescriptions/{id}/print`
- **Payment APIs:** `GET /payments`, `PUT /payments/{id}` (mark paid, apply insurance discount), `GET /payments/{id}/print`
- Role security: Cashier-only for payments

### Person 2
- **Notification APIs:** `POST /notifications` (follow-up reminder), `GET /notifications?userId=X`
- **Booking confirmation:** Auto-send notification after successful appointment booking
- Scheduled job or event-driven reminder creation

---

## Phase 6 — OPD Flow + Redis (Week 6)

| Person 1 | Person 2 |
|---|---|
| Wire Redis: JWT blacklist | View OPD Appointments (Doctor) |
| Wire Redis: appointment slot counter | — |
| Wire Redis: notification real-time | — |

### Person 1
- **Redis JWT blacklist:** Token invalidation on logout
- **Redis appointment slots:** Cache available appointment slots, decrement on booking
- **Redis notifications:** Real-time notification pub/sub or caching layer

### Person 2
- **Doctor Appointment API:** `GET /appointments?doctorId=X` (doctor views own OPD appointments for today)

---

## Git Workflow

### Branch Naming

Format: `<type>/phase-<N>-<short-description>`

| Type | When to use | Example |
|---|---|---|
| `feat/` | New feature work | `feat/phase-2-employee` |
| `fix/` | Bug fix on existing feature | `fix/phase-2-employee-deactivate` |
| `chore/` | Config, CI, docs, deps | `chore/docker-redis-setup` |
| `refactor/` | Code restructure, no behavior change | `refactor/base-entity-uuid` |

```
main  (protected — never push directly)
 ├── feat/phase-1-foundation     (Both)
 ├── feat/phase-2-employee       (Person 1)
 ├── feat/phase-2-pharmacy       (Person 2)
 ├── feat/phase-3-records        (Person 1)
 ├── feat/phase-3-queue          (Person 2)
 └── ...
```

### Commit Messages — Conventional Commits

Format: `<type>(<scope>): <short summary>`

| Type | Meaning | Example |
|---|---|---|
| `feat` | New feature | `feat(employee): add deactivate endpoint` |
| `fix` | Bug fix | `fix(auth): correct role extraction from JWT` |
| `refactor` | Restructure, no behavior change | `refactor(entity): switch ID to UUID` |
| `chore` | Config, build, deps | `chore(docker): add Redis to compose` |
| `docs` | Documentation only | `docs: update API endpoint list` |
| `test` | Add or fix tests | `test(employee): add unit tests for CRUD` |

**Rules:**
- Keep the summary under **72 characters**
- Use **imperative mood** ("add", not "added" or "adds")
- Scope is optional but encouraged — use the domain name (e.g., `auth`, `employee`, `medicine`, `appointment`)
- One commit = one logical change. Don't bundle unrelated work

### Merge & PR Rules

1. **Never push directly to `main`** — always use a PR / merge request
2. **At least 1 review** from the other person before merging
3. **All tests must pass** (`mvn test`) before merging
4. **Squash merge** feature branches into `main` to keep history clean
5. **Delete the branch** after merging

### Do NOT

- ❌ Commit `.env`, secrets, or IDE-specific files (already in `.gitignore`)
- ❌ Force-push to `main`
- ❌ Merge your own PR without the other person reviewing
- ❌ Leave dead / commented-out code in commits
- ❌ Commit with generic messages like `"fix"`, `"update"`, or `"wip"`

## Verification

- **Each phase:** `cd hms-api && mvn test` — all tests pass before merging
- **Phase 6:** Full Swagger walkthrough, fresh DB migration test, role-based access audit
