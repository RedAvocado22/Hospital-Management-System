-- =============================================================
-- V3__add_email_to_account_table.sql
-- =============================================================

ALTER TABLE `account`
    ADD COLUMN `email` varchar(100) DEFAULT NULL;
ALTER TABLE `account`
    ADD CONSTRAINT `UK_account_email` UNIQUE (`email`);
