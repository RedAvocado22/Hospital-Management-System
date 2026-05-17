-- =============================================================
-- V11__update_appointment_date.sql
-- Update appointment date type
-- =============================================================

ALTER TABLE appointment MODIFY COLUMN date DATE;