-- =============================================================
-- V5__insert_roles_data.sql
-- Migration script to add the initial required roles data
-- =============================================================

INSERT INTO `role` (`id`, `created_at`, `updated_at`, `name`, `description`)
VALUES
    (UUID_TO_BIN(UUID()), NOW(), NOW(), 'admin', 'Administrator role'),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), 'cashier', 'Cashier role'),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), 'doctor', 'Doctor role'),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), 'patient', 'Patient role'),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), 'pharmacist', 'Pharmacist role'),
    (UUID_TO_BIN(UUID()), NOW(), NOW(), 'receptionist', 'Receptionist role');
