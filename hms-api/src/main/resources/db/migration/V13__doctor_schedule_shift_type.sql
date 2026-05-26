ALTER TABLE doctor_schedule
    DROP COLUMN start_time,
    DROP COLUMN end_time,
    ADD COLUMN shift_type ENUM('MORNING', 'AFTERNOON', 'EVENING') NOT NULL AFTER date;
