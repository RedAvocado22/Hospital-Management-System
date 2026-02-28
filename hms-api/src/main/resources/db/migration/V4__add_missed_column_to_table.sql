-- =============================================================
-- V4__add_missed_column_to_table.sql
-- =============================================================

ALTER TABLE `account`
    ADD COLUMN `first_name` varchar(100) DEFAULT NULL;

ALTER TABLE `account`
    ADD COLUMN `last_name` varchar(100) DEFAULT NULL;