-- =============================================================
-- V12__seed_prescriptions_and_notifications.sql
-- Fills empty tables: medicine_prescription, medicine_item,
-- notification.
-- =============================================================

-- =============================================================
-- Re-declare references from V9
-- =============================================================
SET @acc_doctor1  = UUID_TO_BIN('3c3adff1-1462-4937-af89-b8ccf1e93d52');
SET @acc_doctor2  = UUID_TO_BIN('9af3d47a-53ce-4431-91e9-093974aad553');
SET @acc_patient1 = UUID_TO_BIN('0ce53e6e-04fa-4b57-97a5-cc4aef760e28');
SET @acc_patient2 = UUID_TO_BIN('b6af3a3f-75b1-4dc6-b6ea-51221ac6642f');
SET @acc_patient3 = UUID_TO_BIN('165a9101-3d50-4558-8491-97aaaccbbdfe');

SET @mr1 = UUID_TO_BIN('e0000001-0000-0000-0000-000000000001');
SET @mr2 = UUID_TO_BIN('e0000001-0000-0000-0000-000000000002');
SET @mr4 = UUID_TO_BIN('e0000001-0000-0000-0000-000000000004');
SET @mr5 = UUID_TO_BIN('e0000001-0000-0000-0000-000000000005');
SET @mr8 = UUID_TO_BIN('e0000001-0000-0000-0000-000000000008');

-- =============================================================
-- Resolve medicine invoice IDs by record
-- =============================================================
SET @mi1 = (SELECT id FROM medicine_invoice WHERE record_id = @mr1 LIMIT 1);
SET @mi2 = (SELECT id FROM medicine_invoice WHERE record_id = @mr2 LIMIT 1);
SET @mi4 = (SELECT id FROM medicine_invoice WHERE record_id = @mr4 LIMIT 1);
SET @mi5 = (SELECT id FROM medicine_invoice WHERE record_id = @mr5 LIMIT 1);
SET @mi8 = (SELECT id FROM medicine_invoice WHERE record_id = @mr8 LIMIT 1);

-- =============================================================
-- Resolve medicine IDs by name
-- =============================================================
SET @med_amoxicillin  = (SELECT id FROM medicine WHERE name = 'Amoxicillin 500mg'      LIMIT 1);
SET @med_paracetamol  = (SELECT id FROM medicine WHERE name = 'Paracetamol 500mg'      LIMIT 1);
SET @med_amlodipine   = (SELECT id FROM medicine WHERE name = 'Amlodipine 5mg'         LIMIT 1);
SET @med_lisinopril   = (SELECT id FROM medicine WHERE name = 'Lisinopril 10mg'        LIMIT 1);
SET @med_ibuprofen    = (SELECT id FROM medicine WHERE name = 'Ibuprofen 400mg'        LIMIT 1);
SET @med_cephalexin   = (SELECT id FROM medicine WHERE name = 'Cephalexin 500mg'       LIMIT 1);
SET @med_cetirizine   = (SELECT id FROM medicine WHERE name = 'Cetirizine 10mg'        LIMIT 1);
SET @med_prednisolone = (SELECT id FROM medicine WHERE name = 'Prednisolone 5mg'       LIMIT 1);
SET @med_metformin    = (SELECT id FROM medicine WHERE name = 'Metformin 500mg'        LIMIT 1);
SET @med_insulin      = (SELECT id FROM medicine WHERE name = 'Insulin Glargine 100IU/mL' LIMIT 1);

-- =============================================================
-- Medicine prescriptions
-- Fixed UUIDs so medicine_item can reference them directly.
-- =============================================================
INSERT INTO `medicine_prescription` (`id`, `created_at`, `updated_at`, `doctor_id`, `medicine_invoice_id`)
VALUES
    -- mr1: Bui Thi Mai — cough and fever (doctor1)
    (UUID_TO_BIN('a0000001-0000-0000-0000-000000000001'), '2025-11-01 09:30:00', '2025-11-01 09:30:00',
     @acc_doctor1, @mi1),

    -- mr2: Vo Van Nam — blood pressure (doctor1)
    (UUID_TO_BIN('a0000001-0000-0000-0000-000000000002'), '2025-11-15 11:00:00', '2025-11-15 11:00:00',
     @acc_doctor1, @mi2),

    -- mr4: Dang Thi Lan — post-surgery follow-up (doctor2)
    (UUID_TO_BIN('a0000001-0000-0000-0000-000000000004'), '2025-12-20 14:30:00', '2025-12-20 14:30:00',
     @acc_doctor2, @mi4),

    -- mr5: Dang Thi Lan — allergic reaction (doctor1)
    (UUID_TO_BIN('a0000001-0000-0000-0000-000000000005'), '2026-01-08 11:30:00', '2026-01-08 11:30:00',
     @acc_doctor1, @mi5),

    -- mr8: Bui Thi Mai — diabetes (doctor2)
    (UUID_TO_BIN('a0000001-0000-0000-0000-000000000008'), '2026-03-05 13:30:00', '2026-03-05 13:30:00',
     @acc_doctor2, @mi8);

SET @p1 = UUID_TO_BIN('a0000001-0000-0000-0000-000000000001');
SET @p2 = UUID_TO_BIN('a0000001-0000-0000-0000-000000000002');
SET @p4 = UUID_TO_BIN('a0000001-0000-0000-0000-000000000004');
SET @p5 = UUID_TO_BIN('a0000001-0000-0000-0000-000000000005');
SET @p8 = UUID_TO_BIN('a0000001-0000-0000-0000-000000000008');

-- =============================================================
-- Medicine items
-- =============================================================
INSERT INTO `medicine_item` (`medicine_id`, `medicine_prescription_id`, `quantity`)
VALUES
    -- Prescription 1: cough + fever → antibiotic + paracetamol
    (@med_amoxicillin,  @p1, 14),
    (@med_paracetamol,  @p1, 10),

    -- Prescription 2: hypertension → antihypertensives
    (@med_amlodipine,   @p2, 30),
    (@med_lisinopril,   @p2, 30),

    -- Prescription 4: post-surgery → pain relief + antibiotic
    (@med_ibuprofen,    @p4, 10),
    (@med_cephalexin,   @p4, 14),

    -- Prescription 5: allergic reaction → antihistamine + steroid
    (@med_cetirizine,   @p5, 10),
    (@med_prednisolone, @p5, 5),

    -- Prescription 8: diabetes → metformin + insulin
    (@med_metformin,    @p8, 60),
    (@med_insulin,      @p8, 2);

-- =============================================================
-- Notifications
-- =============================================================
INSERT INTO `notification` (`id`, `created_at`, `updated_at`, `is_read`, `title`, `message`, `type`, `payload`, `recipient_id`)
VALUES
    -- Appointment confirmed for patient 1
    (UUID_TO_BIN(UUID()), '2026-04-06 10:00:00', '2026-04-06 10:00:00',
     1, 'Appointment Confirmed',
     'Your appointment with Dr. Nguyen Minh on April 7, 2026 at 08:00 has been confirmed.',
     'APPOINTMENT_CONFIRMED', NULL, @acc_patient1),

    -- Appointment reminder for patient 1
    (UUID_TO_BIN(UUID()), '2026-04-06 08:00:00', '2026-04-06 08:00:00',
     0, 'Appointment Reminder',
     'Reminder: You have an appointment tomorrow, April 7, 2026 at 08:00 with Dr. Nguyen Minh.',
     'APPOINTMENT_REMINDER', NULL, @acc_patient1),

    -- Appointment confirmed for patient 2
    (UUID_TO_BIN(UUID()), '2026-04-06 10:05:00', '2026-04-06 10:05:00',
     1, 'Appointment Confirmed',
     'Your appointment with Dr. Nguyen Minh on April 7, 2026 at 08:00 has been confirmed.',
     'APPOINTMENT_CONFIRMED', NULL, @acc_patient2),

    -- Appointment cancelled for patient 2
    (UUID_TO_BIN(UUID()), '2026-04-07 09:00:00', '2026-04-07 09:00:00',
     0, 'Appointment Cancelled',
     'Your appointment with Dr. Tran Linh on April 8, 2026 has been cancelled.',
     'APPOINTMENT_CANCELLED', NULL, @acc_patient2),

    -- Appointment confirmed for patient 3
    (UUID_TO_BIN(UUID()), '2026-04-06 10:10:00', '2026-04-06 10:10:00',
     0, 'Appointment Confirmed',
     'Your appointment with Dr. Tran Linh on April 7, 2026 at 13:00 has been confirmed.',
     'APPOINTMENT_CONFIRMED', NULL, @acc_patient3),

    -- Payment reminder for patient 1
    (UUID_TO_BIN(UUID()), '2026-03-06 09:00:00', '2026-03-06 09:00:00',
     0, 'Invoice Due',
     'Your medical invoice from March 5, 2026 is due for payment. Please visit the cashier.',
     'INVOICE_DUE', NULL, @acc_patient1),

    -- Payment reminder for patient 2
    (UUID_TO_BIN(UUID()), '2026-01-26 09:00:00', '2026-01-26 09:00:00',
     1, 'Invoice Due',
     'Your medical invoice from January 25, 2026 is due for payment. Please visit the cashier.',
     'INVOICE_DUE', NULL, @acc_patient2);
