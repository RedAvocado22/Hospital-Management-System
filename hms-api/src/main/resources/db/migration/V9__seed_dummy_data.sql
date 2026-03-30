-- =============================================================
-- V9__seed_dummy_data.sql
-- Full seed: accounts, employees, patients, schedules,
-- appointments, medical records, invoices.
--
-- IMPORTANT: account.id values match Keycloak realm UUIDs.
-- UserContext.userId = JWT sub = Keycloak UUID, and ABAC filters
-- compare that against doctor_id/patient FK — they must match.
-- =============================================================

-- =============================================================
-- Roles (seeded by V5)
-- =============================================================
SET @role_admin        = (SELECT id FROM `role` WHERE name = 'admin');
SET @role_doctor       = (SELECT id FROM `role` WHERE name = 'doctor');
SET @role_receptionist = (SELECT id FROM `role` WHERE name = 'receptionist');
SET @role_pharmacist   = (SELECT id FROM `role` WHERE name = 'pharmacist');
SET @role_cashier      = (SELECT id FROM `role` WHERE name = 'cashier');
SET @role_patient      = (SELECT id FROM `role` WHERE name = 'patient');

-- =============================================================
-- Departments (seeded by V6)
-- =============================================================
SET @dept_internal   = (SELECT id FROM `department` WHERE name = 'Internal Medicine');
SET @dept_surgery    = (SELECT id FROM `department` WHERE name = 'Surgery');
SET @dept_pediatrics = (SELECT id FROM `department` WHERE name = 'Pediatrics');
SET @dept_emergency  = (SELECT id FROM `department` WHERE name = 'Emergency');

-- =============================================================
-- Accounts
-- IDs match Keycloak realm UUIDs — required for JWT ABAC.
-- =============================================================
INSERT INTO `account` (`id`, `created_at`, `updated_at`, `username`, `first_name`, `last_name`, `full_name`,
                       `email`, `dob`, `gender`, `address`, `phone`, `is_active`, `is_synced`, `synced_at`, `role_id`)
VALUES
    -- ADMIN
    (UUID_TO_BIN('9f76e813-2be5-44e5-97c0-29262613765e'), NOW(), NOW(),
     'admin.seed', 'Admin', 'Seed', 'Admin Seed',
     'admin.seed@hms.dev', '1985-03-15', 'MALE',
     '1 Hospital Admin St, Ho Chi Minh City', '0901000001', 1, 0, NULL, @role_admin),

    -- DOCTOR 1 — Internal Medicine
    (UUID_TO_BIN('3c3adff1-1462-4937-af89-b8ccf1e93d52'), NOW(), NOW(),
     'dr.nguyen', 'Nguyen', 'Minh', 'Nguyen Minh',
     'dr.nguyen@hms.dev', '1978-05-10', 'MALE',
     '45 Le Loi, District 1, Ho Chi Minh City', '0901000002', 1, 0, NULL, @role_doctor),

    -- DOCTOR 2 — Surgery
    (UUID_TO_BIN('9af3d47a-53ce-4431-91e9-093974aad553'), NOW(), NOW(),
     'dr.tran', 'Tran', 'Linh', 'Tran Linh',
     'dr.tran@hms.dev', '1982-11-25', 'FEMALE',
     '88 Pasteur, District 3, Ho Chi Minh City', '0901000003', 1, 0, NULL, @role_doctor),

    -- RECEPTIONIST
    (UUID_TO_BIN('85be681f-363c-46c6-a6da-45437bcde7f3'), NOW(), NOW(),
     'receptionist.le', 'Le', 'Huong', 'Le Huong',
     'receptionist.le@hms.dev', '1995-02-28', 'FEMALE',
     '9 Nam Ky Khoi Nghia, District 3, Ho Chi Minh City', '0901000004', 1, 0, NULL, @role_receptionist),

    -- PHARMACIST
    (UUID_TO_BIN('ccec1719-7eae-4235-8bfe-8b2813acee15'), NOW(), NOW(),
     'pharmacist.pham', 'Pham', 'Duc', 'Pham Duc',
     'pharmacist.pham@hms.dev', '1988-06-15', 'MALE',
     '33 Ly Tu Trong, District 1, Ho Chi Minh City', '0901000005', 1, 0, NULL, @role_pharmacist),

    -- CASHIER
    (UUID_TO_BIN('5bcd1221-2085-4d62-8f42-2d42792a52eb'), NOW(), NOW(),
     'cashier.hoang', 'Hoang', 'Anh', 'Hoang Anh',
     'cashier.hoang@hms.dev', '1992-08-20', 'FEMALE',
     '12 Nguyen Hue, District 1, Ho Chi Minh City', '0901000006', 1, 0, NULL, @role_cashier),

    -- PATIENT 1 — Bui Thi Mai
    (UUID_TO_BIN('0ce53e6e-04fa-4b57-97a5-cc4aef760e28'), NOW(), NOW(),
     'patient.bui', 'Bui', 'Thi Mai', 'Bui Thi Mai',
     'patient.bui@hms.dev', '1990-04-12', 'FEMALE',
     '22 Tran Hung Dao, District 5, Ho Chi Minh City', '0901000007', 1, 0, NULL, @role_patient),

    -- PATIENT 2 — Vo Van Nam
    (UUID_TO_BIN('b6af3a3f-75b1-4dc6-b6ea-51221ac6642f'), NOW(), NOW(),
     'patient.vo', 'Vo', 'Van Nam', 'Vo Van Nam',
     'patient.vo@hms.dev', '1975-01-08', 'MALE',
     '77 Dien Bien Phu, Binh Thanh, Ho Chi Minh City', '0901000008', 1, 0, NULL, @role_patient),

    -- PATIENT 3 — Dang Thi Lan
    (UUID_TO_BIN('165a9101-3d50-4558-8491-97aaaccbbdfe'), NOW(), NOW(),
     'patient.dang', 'Dang', 'Thi Lan', 'Dang Thi Lan',
     'patient.dang@hms.dev', '1985-07-30', 'FEMALE',
     '5 Vo Van Tan, District 3, Ho Chi Minh City', '0901000009', 1, 0, NULL, @role_patient);

-- Shorthand variables
SET @acc_admin        = UUID_TO_BIN('9f76e813-2be5-44e5-97c0-29262613765e');
SET @acc_doctor1      = UUID_TO_BIN('3c3adff1-1462-4937-af89-b8ccf1e93d52');
SET @acc_doctor2      = UUID_TO_BIN('9af3d47a-53ce-4431-91e9-093974aad553');
SET @acc_receptionist = UUID_TO_BIN('85be681f-363c-46c6-a6da-45437bcde7f3');
SET @acc_pharmacist   = UUID_TO_BIN('ccec1719-7eae-4235-8bfe-8b2813acee15');
SET @acc_cashier      = UUID_TO_BIN('5bcd1221-2085-4d62-8f42-2d42792a52eb');
SET @acc_patient1     = UUID_TO_BIN('0ce53e6e-04fa-4b57-97a5-cc4aef760e28');
SET @acc_patient2     = UUID_TO_BIN('b6af3a3f-75b1-4dc6-b6ea-51221ac6642f');
SET @acc_patient3     = UUID_TO_BIN('165a9101-3d50-4558-8491-97aaaccbbdfe');

-- =============================================================
-- Employee info
-- =============================================================
INSERT INTO `employee_info` (`id`, `created_at`, `updated_at`, `code`, `hire_date`, `account_id`, `department_id`)
VALUES
    (UUID_TO_BIN(UUID()), NOW(), NOW(), 'EMP-ADMIN-001', '2020-01-01', @acc_admin,        @dept_emergency),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), 'EMP-DOC-001',   '2018-06-01', @acc_doctor1,      @dept_internal),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), 'EMP-DOC-002',   '2016-09-15', @acc_doctor2,      @dept_surgery),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), 'EMP-REC-001',   '2022-03-10', @acc_receptionist, @dept_emergency),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), 'EMP-PHA-001',   '2021-07-20', @acc_pharmacist,   @dept_internal),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), 'EMP-CAS-001',   '2023-01-05', @acc_cashier,      @dept_pediatrics);

-- =============================================================
-- Patient info
-- Fixed UUIDs so downstream FKs (appointments, records) can
-- reference them directly without a subquery.
-- =============================================================
INSERT INTO `patient_info` (`id`, `created_at`, `updated_at`, `blood_type`, `allergies`, `account_id`)
VALUES
    (UUID_TO_BIN('b1000001-0000-0000-0000-000000000001'), NOW(), NOW(),
     'A+', 'Penicillin', @acc_patient1),

    (UUID_TO_BIN('b1000001-0000-0000-0000-000000000002'), NOW(), NOW(),
     'B+', 'Aspirin, Pollen', @acc_patient2),

    (UUID_TO_BIN('b1000001-0000-0000-0000-000000000003'), NOW(), NOW(),
     'O+', NULL, @acc_patient3);

SET @pi1 = UUID_TO_BIN('b1000001-0000-0000-0000-000000000001');
SET @pi2 = UUID_TO_BIN('b1000001-0000-0000-0000-000000000002');
SET @pi3 = UUID_TO_BIN('b1000001-0000-0000-0000-000000000003');

-- =============================================================
-- Health insurance
-- =============================================================
INSERT INTO `health_insurance` (`id`, `created_at`, `updated_at`, `name`, `is_valid`, `due_date`, `patient_id`)
VALUES
    (UUID_TO_BIN(UUID()), NOW(), NOW(), 'BHYT-BUI-2026',   1, '2026-12-31', @pi1),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), 'BHYT-VO-2026',    1, '2026-06-30', @pi2),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), 'BHYT-DANG-2024',  0, '2025-03-01', @pi3);

-- =============================================================
-- Hospital services
-- =============================================================
INSERT INTO `service` (`id`, `created_at`, `updated_at`, `name`, `price`)
VALUES
    (UUID_TO_BIN('c0000001-0000-0000-0000-000000000001'), NOW(), NOW(), 'General Consultation', 150000.00),
    (UUID_TO_BIN('c0000001-0000-0000-0000-000000000002'), NOW(), NOW(), 'Blood Test',           250000.00),
    (UUID_TO_BIN('c0000001-0000-0000-0000-000000000003'), NOW(), NOW(), 'X-Ray',                400000.00),
    (UUID_TO_BIN('c0000001-0000-0000-0000-000000000004'), NOW(), NOW(), 'Ultrasound',           500000.00),
    (UUID_TO_BIN('c0000001-0000-0000-0000-000000000005'), NOW(), NOW(), 'ECG',                  300000.00);

SET @svc_consult    = UUID_TO_BIN('c0000001-0000-0000-0000-000000000001');
SET @svc_blood      = UUID_TO_BIN('c0000001-0000-0000-0000-000000000002');
SET @svc_xray       = UUID_TO_BIN('c0000001-0000-0000-0000-000000000003');
SET @svc_ultrasound = UUID_TO_BIN('c0000001-0000-0000-0000-000000000004');
SET @svc_ecg        = UUID_TO_BIN('c0000001-0000-0000-0000-000000000005');

-- =============================================================
-- Doctor schedules
-- Fixed UUIDs for appointment FK references.
-- =============================================================
INSERT INTO `doctor_schedule` (`id`, `created_at`, `updated_at`, `date`, `start_time`, `end_time`,
                               `max_patients`, `is_available`, `doctor_id`)
VALUES
    (UUID_TO_BIN('d0000001-0000-0000-0000-000000000001'), NOW(), NOW(),
     '2026-04-07', '08:00:00', '12:00:00', 10, 1, @acc_doctor1),

    (UUID_TO_BIN('d0000001-0000-0000-0000-000000000002'), NOW(), NOW(),
     '2026-04-08', '13:00:00', '17:00:00', 10, 1, @acc_doctor1),

    (UUID_TO_BIN('d0000001-0000-0000-0000-000000000003'), NOW(), NOW(),
     '2026-04-09', '08:00:00', '12:00:00',  8, 1, @acc_doctor1),

    (UUID_TO_BIN('d0000001-0000-0000-0000-000000000004'), NOW(), NOW(),
     '2026-04-07', '13:00:00', '17:00:00', 10, 1, @acc_doctor2),

    (UUID_TO_BIN('d0000001-0000-0000-0000-000000000005'), NOW(), NOW(),
     '2026-04-08', '08:00:00', '12:00:00', 10, 1, @acc_doctor2),

    (UUID_TO_BIN('d0000001-0000-0000-0000-000000000006'), NOW(), NOW(),
     '2026-04-10', '08:00:00', '12:00:00',  8, 1, @acc_doctor2);

SET @sched1 = UUID_TO_BIN('d0000001-0000-0000-0000-000000000001');
SET @sched2 = UUID_TO_BIN('d0000001-0000-0000-0000-000000000002');
SET @sched4 = UUID_TO_BIN('d0000001-0000-0000-0000-000000000004');
SET @sched5 = UUID_TO_BIN('d0000001-0000-0000-0000-000000000005');

-- =============================================================
-- Appointments
-- date column is datetime(6) here; V11 converts it to DATE.
-- =============================================================
INSERT INTO `appointment` (`id`, `created_at`, `updated_at`, `date`, `reason`, `status`,
                           `doctor_id`, `patient_id`, `schedule_id`)
VALUES
    (UUID_TO_BIN(UUID()), NOW(), NOW(), '2026-04-07',
     'Follow-up for persistent cough', 'CONFIRMED',
     @acc_doctor1, @pi1, @sched1),

    (UUID_TO_BIN(UUID()), NOW(), NOW(), '2026-04-07',
     'Routine blood pressure check', 'PENDING',
     @acc_doctor1, @pi2, @sched1),

    (UUID_TO_BIN(UUID()), NOW(), NOW(), '2026-04-07',
     'Post-surgery wound review', 'CONFIRMED',
     @acc_doctor2, @pi3, @sched4),

    (UUID_TO_BIN(UUID()), NOW(), NOW(), '2026-04-08',
     'Diabetes management consultation', 'PENDING',
     @acc_doctor1, @pi1, @sched2),

    (UUID_TO_BIN(UUID()), NOW(), NOW(), '2026-04-08',
     'Back pain follow-up', 'CANCELLED',
     @acc_doctor2, @pi2, @sched5);

-- =============================================================
-- Medical records
-- Fixed UUIDs so service/medicine invoices can reference them.
-- =============================================================
INSERT INTO `medical_record` (`id`, `created_at`, `updated_at`, `description`, `doctor_advice`,
                              `doctor_id`, `patient_id`)
VALUES
    (UUID_TO_BIN('e0000001-0000-0000-0000-000000000001'), '2025-11-01 09:00:00', '2025-11-01 09:00:00',
     'Patient presents with persistent cough and mild fever.',
     'Rest for 3 days. Take prescribed antibiotics. Return if fever exceeds 39C.',
     @acc_doctor1, @pi1),

    (UUID_TO_BIN('e0000001-0000-0000-0000-000000000002'), '2025-11-15 10:30:00', '2025-11-15 10:30:00',
     'Routine checkup. Blood pressure slightly elevated at 140/90.',
     'Reduce salt intake. Monitor blood pressure daily. Follow up in 2 weeks.',
     @acc_doctor1, @pi2),

    (UUID_TO_BIN('e0000001-0000-0000-0000-000000000003'), '2025-12-03 08:15:00', '2025-12-03 08:15:00',
     'Patient reports sharp abdominal pain, nausea for 2 days.',
     'Ultrasound ordered. Avoid fatty foods. NPO until further notice.',
     @acc_doctor2, @pi1),

    (UUID_TO_BIN('e0000001-0000-0000-0000-000000000004'), '2025-12-20 14:00:00', '2025-12-20 14:00:00',
     'Post-surgery follow-up. Wound healing well, no signs of infection.',
     'Continue wound dressing daily. Avoid strenuous activity for 4 weeks.',
     @acc_doctor2, @pi3),

    (UUID_TO_BIN('e0000001-0000-0000-0000-000000000005'), '2026-01-08 11:00:00', '2026-01-08 11:00:00',
     'Allergic reaction — widespread hives after taking new medication.',
     'Discontinue current medication immediately. Prescribe antihistamine.',
     @acc_doctor1, @pi3),

    (UUID_TO_BIN('e0000001-0000-0000-0000-000000000006'), '2026-01-25 09:45:00', '2026-01-25 09:45:00',
     'Chronic lower back pain. MRI shows mild disc herniation at L4-L5.',
     'Physical therapy 3x per week. Avoid heavy lifting. Pain relief as needed.',
     @acc_doctor2, @pi2),

    (UUID_TO_BIN('e0000001-0000-0000-0000-000000000007'), '2026-02-10 16:30:00', '2026-02-10 16:30:00',
     'Annual health screening. All vitals within normal range.',
     'Maintain current lifestyle. Repeat screening in 12 months.',
     @acc_doctor1, @pi2),

    (UUID_TO_BIN('e0000001-0000-0000-0000-000000000008'), '2026-03-05 13:00:00', '2026-03-05 13:00:00',
     'Patient with type 2 diabetes, HbA1c at 8.2% — above target.',
     'Adjust insulin dosage. Strict low-carb diet. Weekly glucose monitoring.',
     @acc_doctor2, @pi1);

SET @mr1 = UUID_TO_BIN('e0000001-0000-0000-0000-000000000001');
SET @mr2 = UUID_TO_BIN('e0000001-0000-0000-0000-000000000002');
SET @mr3 = UUID_TO_BIN('e0000001-0000-0000-0000-000000000003');
SET @mr4 = UUID_TO_BIN('e0000001-0000-0000-0000-000000000004');
SET @mr5 = UUID_TO_BIN('e0000001-0000-0000-0000-000000000005');
SET @mr6 = UUID_TO_BIN('e0000001-0000-0000-0000-000000000006');
SET @mr7 = UUID_TO_BIN('e0000001-0000-0000-0000-000000000007');
SET @mr8 = UUID_TO_BIN('e0000001-0000-0000-0000-000000000008');

-- =============================================================
-- Service invoices (one per medical record)
-- =============================================================
INSERT INTO `service_invoice` (`id`, `created_at`, `updated_at`, `due_date`, `total_fee`, `medical_record_id`)
VALUES
    (UUID_TO_BIN('f1000001-0000-0000-0000-000000000001'), NOW(), NOW(), '2025-12-01',  400000.00, @mr1),
    (UUID_TO_BIN('f1000001-0000-0000-0000-000000000002'), NOW(), NOW(), '2025-12-15',  650000.00, @mr2),
    (UUID_TO_BIN('f1000001-0000-0000-0000-000000000003'), NOW(), NOW(), '2026-01-03',  900000.00, @mr3),
    (UUID_TO_BIN('f1000001-0000-0000-0000-000000000004'), NOW(), NOW(), '2026-01-20',  150000.00, @mr4),
    (UUID_TO_BIN('f1000001-0000-0000-0000-000000000005'), NOW(), NOW(), '2026-02-08',  150000.00, @mr5),
    (UUID_TO_BIN('f1000001-0000-0000-0000-000000000006'), NOW(), NOW(), '2026-02-25',  700000.00, @mr6),
    (UUID_TO_BIN('f1000001-0000-0000-0000-000000000007'), NOW(), NOW(), '2026-03-10',  150000.00, @mr7),
    (UUID_TO_BIN('f1000001-0000-0000-0000-000000000008'), NOW(), NOW(), '2026-04-05',  400000.00, @mr8);

SET @si1 = UUID_TO_BIN('f1000001-0000-0000-0000-000000000001');
SET @si2 = UUID_TO_BIN('f1000001-0000-0000-0000-000000000002');
SET @si3 = UUID_TO_BIN('f1000001-0000-0000-0000-000000000003');
SET @si4 = UUID_TO_BIN('f1000001-0000-0000-0000-000000000004');
SET @si5 = UUID_TO_BIN('f1000001-0000-0000-0000-000000000005');
SET @si6 = UUID_TO_BIN('f1000001-0000-0000-0000-000000000006');
SET @si7 = UUID_TO_BIN('f1000001-0000-0000-0000-000000000007');
SET @si8 = UUID_TO_BIN('f1000001-0000-0000-0000-000000000008');

-- =============================================================
-- Service items
-- =============================================================
INSERT INTO `service_item` (`service_id`, `service_invoice_id`, `number_service_use`)
VALUES
    (@svc_consult,    @si1, 1),
    (@svc_blood,      @si1, 1),
    (@svc_consult,    @si2, 1),
    (@svc_blood,      @si2, 1),
    (@svc_ecg,        @si2, 1),
    (@svc_consult,    @si3, 1),
    (@svc_ultrasound, @si3, 1),
    (@svc_consult,    @si4, 1),
    (@svc_consult,    @si5, 1),
    (@svc_consult,    @si6, 1),
    (@svc_xray,       @si6, 1),
    (@svc_consult,    @si7, 1),
    (@svc_consult,    @si8, 1),
    (@svc_blood,      @si8, 1);

-- =============================================================
-- Medicine invoices (one per medical record, mirrors
-- CreateMedicalRecordService auto-creation behaviour)
-- =============================================================
INSERT INTO `medicine_invoice` (`id`, `created_at`, `updated_at`, `due_date`, `is_paid`, `total_price`, `record_id`)
VALUES
    (UUID_TO_BIN(UUID()), NOW(), NOW(), '2025-12-01', 1,  120000.00, @mr1),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), '2025-12-15', 1,   85000.00, @mr2),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), '2026-01-03', 0,       NULL, @mr3),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), '2026-01-20', 1,   60000.00, @mr4),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), '2026-02-08', 1,   45000.00, @mr5),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), '2026-02-25', 0,       NULL, @mr6),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), '2026-03-10', 1,       0.00, @mr7),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), '2026-04-05', 0,       NULL, @mr8);

-- =============================================================
-- Medical examination history
-- =============================================================
INSERT INTO `medical_examination_history` (`id`, `created_at`, `updated_at`, `date`, `description`,
                                           `patient_id`, `reception_counter_staff_id`)
VALUES
    (UUID_TO_BIN(UUID()), NOW(), NOW(), '2025-11-01',
     'Patient checked in for cough and fever. Triaged to Internal Medicine.',
     @pi1, @acc_receptionist),

    (UUID_TO_BIN(UUID()), NOW(), NOW(), '2025-11-15',
     'Routine annual checkup visit. Directed to Internal Medicine.',
     @pi2, @acc_receptionist),

    (UUID_TO_BIN(UUID()), NOW(), NOW(), '2025-12-03',
     'Acute abdominal pain. Referred urgently to Surgery.',
     @pi1, @acc_receptionist),

    (UUID_TO_BIN(UUID()), NOW(), NOW(), '2025-12-20',
     'Post-operative follow-up. Wound check scheduled.',
     @pi3, @acc_receptionist);

-- =============================================================
-- Hospital fee payments (settled records)
-- =============================================================
INSERT INTO `hospital_fee_payment` (`id`, `created_at`, `updated_at`, `due_date`, `is_paid`, `total_fee`,
                                    `cashier_counter_staff_id`, `record_id`, `patient_id`)
VALUES
    (UUID_TO_BIN(UUID()), NOW(), NOW(), '2025-12-01', 1,  520000.00, @acc_cashier, @mr1, @pi1),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), '2025-12-15', 1,  735000.00, @acc_cashier, @mr2, @pi2),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), '2026-01-20', 1,  210000.00, @acc_cashier, @mr4, @pi3),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), '2026-02-08', 1,  195000.00, @acc_cashier, @mr5, @pi3),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), '2026-03-10', 1,  150000.00, @acc_cashier, @mr7, @pi2);
