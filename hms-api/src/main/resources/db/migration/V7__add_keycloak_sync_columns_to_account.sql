-- =============================================================
-- V7__add_keycloak_sync_columns_to_account.sql
-- Add Keycloak sync tracking columns to the account table
-- =============================================================

ALTER TABLE `account`
    ADD COLUMN `is_synced` TINYINT(1) NOT NULL DEFAULT 0;

ALTER TABLE `account`
    ADD COLUMN `synced_at` DATETIME(6) DEFAULT NULL;

ALTER TABLE `account`
    ADD INDEX `idx_account_is_synced` (`is_synced`);