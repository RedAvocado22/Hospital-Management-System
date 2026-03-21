-- =============================================================
-- V9__seed_dummy_data.sql
-- Seed employee_info, patient_info, and medical_record.
-- Accounts already exist via API registration.
-- =============================================================

-- -----------------------------------------------
-- Resolve account IDs by username
-- -----------------------------------------------
SET @acc_admin          = (SELECT id FROM `account` WHERE username = 'admin.seed');
SET @acc_doctor1        = (SELECT id FROM `account` WHERE username = 'dr.nguyen');
SET @acc_doctor2        = (SELECT id FROM `account` WHERE username = 'dr.tran');
SET @acc_receptionist   = (SELECT id FROM `account` WHERE username = 'receptionist.le');
SET @acc_pharmacist     = (SELECT id FROM `account` WHERE username = 'pharmacist.pham');
SET @acc_cashier        = (SELECT id FROM `account` WHERE username = 'cashier.hoang');
SET @acc_patient1       = (SELECT id FROM `account` WHERE username = 'patient.bui');
SET @acc_patient2       = (SELECT id FROM `account` WHERE username = 'patient.vo');
SET @acc_patient3       = (SELECT id FROM `account` WHERE username = 'patient.dang');

-- -----------------------------------------------
-- Resolve department IDs by name (set by V6)
-- -----------------------------------------------
SET @dept_internal   = (SELECT id FROM `department` WHERE name = 'Internal Medicine');
SET @dept_surgery    = (SELECT id FROM `department` WHERE name = 'Surgery');
SET @dept_pediatrics = (SELECT id FROM `department` WHERE name = 'Pediatrics');
SET @dept_emergency  = (SELECT id FROM `department` WHERE name = 'Emergency');

-- -----------------------------------------------
-- Employee info
-- -----------------------------------------------
INSERT INTO `employee_info` (`id`, `created_at`, `updated_at`, `code`, `hire_date`, `account_id`, `department_id`)
VALUES
    (UUID_TO_BIN(UUID()), NOW(), NOW(), 'EMP-ADMIN-001', '2020-01-01', @acc_admin,        @dept_emergency),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), 'EMP-DOC-001',   '2018-06-01', @acc_doctor1,      @dept_internal),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), 'EMP-DOC-002',   '2016-09-15', @acc_doctor2,      @dept_surgery),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), 'EMP-REC-001',   '2022-03-10', @acc_receptionist, @dept_emergency),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), 'EMP-PHA-001',   '2021-07-20', @acc_pharmacist,   @dept_internal),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), 'EMP-CAS-001',   '2023-01-05', @acc_cashier,      @dept_pediatrics);

-- -----------------------------------------------
-- Patient info (already created by signup flow)
-- Just look up existing IDs by account
-- -----------------------------------------------
SET @patient_info1 = (SELECT id FROM `patient_info` WHERE account_id = @acc_patient1);
SET @patient_info2 = (SELECT id FROM `patient_info` WHERE account_id = @acc_patient2);
SET @patient_info3 = (SELECT id FROM `patient_info` WHERE account_id = @acc_patient3);

-- -----------------------------------------------
-- Medical records
-- -----------------------------------------------
INSERT INTO `medical_record` (`id`, `created_at`, `updated_at`, `description`, `doctor_advice`, `doctor_id`, `patient_id`)
VALUES
    (UUID_TO_BIN(UUID()), '2025-11-01 09:00:00', '2025-11-01 09:00:00',
     'Patient presents with persistent cough and mild fever.',
     'Rest for 3 days. Take prescribed antibiotics. Return if fever exceeds 39C.',
     @acc_doctor1, @patient_info1),

    (UUID_TO_BIN(UUID()), '2025-11-15 10:30:00', '2025-11-15 10:30:00',
     'Routine checkup. Blood pressure slightly elevated at 140/90.',
     'Reduce salt intake. Monitor blood pressure daily. Follow up in 2 weeks.',
     @acc_doctor1, @patient_info2),

    (UUID_TO_BIN(UUID()), '2025-12-03 08:15:00', '2025-12-03 08:15:00',
     'Patient reports sharp abdominal pain, nausea for 2 days.',
     'Ultrasound ordered. Avoid fatty foods. NPO until further notice.',
     @acc_doctor2, @patient_info1),

    (UUID_TO_BIN(UUID()), '2025-12-20 14:00:00', '2025-12-20 14:00:00',
     'Post-surgery follow-up. Wound healing well, no signs of infection.',
     'Continue wound dressing daily. Avoid strenuous activity for 4 weeks.',
     @acc_doctor2, @patient_info3),

    (UUID_TO_BIN(UUID()), '2026-01-08 11:00:00', '2026-01-08 11:00:00',
     'Allergic reaction — widespread hives after taking new medication.',
     'Discontinue current medication immediately. Prescribe antihistamine.',
     @acc_doctor1, @patient_info3),

    (UUID_TO_BIN(UUID()), '2026-01-25 09:45:00', '2026-01-25 09:45:00',
     'Chronic lower back pain. MRI shows mild disc herniation at L4-L5.',
     'Physical therapy 3x per week. Avoid heavy lifting. Pain relief as needed.',
     @acc_doctor2, @patient_info2),

    (UUID_TO_BIN(UUID()), '2026-02-10 16:30:00', '2026-02-10 16:30:00',
     'Annual health screening. All vitals within normal range.',
     'Maintain current lifestyle. Repeat screening in 12 months.',
     @acc_doctor1, @patient_info2),

    (UUID_TO_BIN(UUID()), '2026-03-05 13:00:00', '2026-03-05 13:00:00',
     'Patient with type 2 diabetes, HbA1c at 8.2% — above target.',
     'Adjust insulin dosage. Strict low-carb diet. Weekly glucose monitoring.',
     @acc_doctor2, @patient_info1);
