-- =============================================================
-- V1__init.sql — Initial HMS database schema
-- =============================================================
-- This migration creates a minimal starting schema.
-- Add domain tables (patients, doctors, appointments, etc.)
-- in subsequent versioned migrations (V2__, V3__, ...).
-- =============================================================

-- Example: a simple lookup table to verify Flyway is working
CREATE TABLE IF NOT EXISTS app_metadata (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    meta_key    VARCHAR(100) NOT NULL UNIQUE,
    meta_value  TEXT,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO app_metadata (meta_key, meta_value) VALUES ('schema_version', '1.0.0');
INSERT INTO app_metadata (meta_key, meta_value) VALUES ('app_name', 'Hospital Management System');
