-- =============================================================
-- V6__insert_departments_data.sql
-- Migration script to seed the initial department data
-- =============================================================

INSERT INTO `department` (`id`, `created_at`, `updated_at`, `name`, `is_active`)
VALUES (UUID_TO_BIN(UUID()), NOW(), NOW(), 'Internal Medicine', 1),
       (UUID_TO_BIN(UUID()), NOW(), NOW(), 'Surgery', 1),
       (UUID_TO_BIN(UUID()), NOW(), NOW(), 'Obstetrics & Gynecology', 1),
       (UUID_TO_BIN(UUID()), NOW(), NOW(), 'Pediatrics', 1),
       (UUID_TO_BIN(UUID()), NOW(), NOW(), 'Ophthalmology', 1),
       (UUID_TO_BIN(UUID()), NOW(), NOW(), 'ENT (Ear, Nose & Throat)', 1),
       (UUID_TO_BIN(UUID()), NOW(), NOW(), 'Dermatology', 1),
       (UUID_TO_BIN(UUID()), NOW(), NOW(), 'Dental & Maxillofacial', 1),
       (UUID_TO_BIN(UUID()), NOW(), NOW(), 'Emergency', 1),
       (UUID_TO_BIN(UUID()), NOW(), NOW(), 'Laboratory', 1);
