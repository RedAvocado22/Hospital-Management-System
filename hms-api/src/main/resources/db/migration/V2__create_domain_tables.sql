-- =============================================================
-- V2__create_domain_tables.sql
-- Migration script to create all domain tables for HMS.
-- Generated from Hibernate ddl-auto.
-- =============================================================

CREATE TABLE `role` (
  `id` binary(16) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `description` text,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `department` (
  `id` binary(16) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `service` (
  `id` binary(16) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `medicine` (
  `id` binary(16) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `description` text,
  `is_active` tinyint(1) NOT NULL,
  `name` varchar(100) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `account` (
  `id` binary(16) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `full_name` varchar(100) DEFAULT NULL,
  `gender` enum('FEMALE','MALE') DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  `role_id` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_account_username` (`username`),
  KEY `FK_account_role` (`role_id`),
  CONSTRAINT `FK_account_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `employee_info` (
  `id` binary(16) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `code` varchar(20) DEFAULT NULL,
  `hire_date` date DEFAULT NULL,
  `account_id` binary(16) NOT NULL,
  `department_id` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_employee_info_account` (`account_id`),
  KEY `FK_employee_info_department` (`department_id`),
  CONSTRAINT `FK_employee_info_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FK_employee_info_department` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `patient_info` (
  `id` binary(16) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `allergies` text,
  `blood_type` varchar(5) DEFAULT NULL,
  `account_id` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_patient_info_account` (`account_id`),
  CONSTRAINT `FK_patient_info_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `health_insurance` (
  `id` binary(16) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `due_date` date DEFAULT NULL,
  `is_valid` tinyint(1) NOT NULL,
  `name` varchar(100) NOT NULL,
  `patient_id` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_health_insurance_patient` (`patient_id`),
  CONSTRAINT `FK_health_insurance_patient` FOREIGN KEY (`patient_id`) REFERENCES `patient_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `doctor_schedule` (
  `id` binary(16) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `date` date NOT NULL,
  `end_time` time(6) NOT NULL,
  `is_available` tinyint(1) NOT NULL,
  `max_patients` int NOT NULL,
  `start_time` time(6) NOT NULL,
  `doctor_id` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_doctor_schedule_doctor` (`doctor_id`),
  CONSTRAINT `FK_doctor_schedule_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `appointment` (
  `id` binary(16) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `date` datetime(6) NOT NULL,
  `reason` text,
  `status` enum('CANCELLED','COMPLETED','CONFIRMED','PENDING') NOT NULL,
  `doctor_id` binary(16) NOT NULL,
  `patient_id` binary(16) NOT NULL,
  `schedule_id` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_appointment_doctor` (`doctor_id`),
  KEY `FK_appointment_patient` (`patient_id`),
  KEY `FK_appointment_schedule` (`schedule_id`),
  CONSTRAINT `FK_appointment_patient` FOREIGN KEY (`patient_id`) REFERENCES `patient_info` (`id`),
  CONSTRAINT `FK_appointment_schedule` FOREIGN KEY (`schedule_id`) REFERENCES `doctor_schedule` (`id`),
  CONSTRAINT `FK_appointment_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `medical_record` (
  `id` binary(16) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `description` text,
  `doctor_advice` text,
  `doctor_id` binary(16) NOT NULL,
  `patient_id` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_medical_record_doctor` (`doctor_id`),
  KEY `FK_medical_record_patient` (`patient_id`),
  CONSTRAINT `FK_medical_record_patient` FOREIGN KEY (`patient_id`) REFERENCES `patient_info` (`id`),
  CONSTRAINT `FK_medical_record_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `medical_examination_history` (
  `id` binary(16) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `description` text,
  `patient_id` binary(16) NOT NULL,
  `reception_counter_staff_id` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_med_exam_history_patient` (`patient_id`),
  KEY `FK_med_exam_history_reception` (`reception_counter_staff_id`),
  CONSTRAINT `FK_med_exam_history_reception` FOREIGN KEY (`reception_counter_staff_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FK_med_exam_history_patient` FOREIGN KEY (`patient_id`) REFERENCES `patient_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `medicine_invoice` (
  `id` binary(16) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `due_date` date DEFAULT NULL,
  `is_paid` tinyint(1) NOT NULL,
  `total_price` decimal(10,2) DEFAULT NULL,
  `record_id` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_medicine_invoice_record` (`record_id`),
  CONSTRAINT `FK_medicine_invoice_record` FOREIGN KEY (`record_id`) REFERENCES `medical_record` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `medicine_prescription` (
  `id` binary(16) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `doctor_id` binary(16) NOT NULL,
  `medicine_invoice_id` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_med_prescription_doctor` (`doctor_id`),
  KEY `FK_med_prescription_invoice` (`medicine_invoice_id`),
  CONSTRAINT `FK_med_prescription_invoice` FOREIGN KEY (`medicine_invoice_id`) REFERENCES `medicine_invoice` (`id`),
  CONSTRAINT `FK_med_prescription_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `medicine_item` (
  `medicine_id` binary(16) NOT NULL,
  `medicine_prescription_id` binary(16) NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`medicine_id`,`medicine_prescription_id`),
  KEY `FK_medicine_item_prescription` (`medicine_prescription_id`),
  CONSTRAINT `FK_medicine_item_prescription` FOREIGN KEY (`medicine_prescription_id`) REFERENCES `medicine_prescription` (`id`),
  CONSTRAINT `FK_medicine_item_medicine` FOREIGN KEY (`medicine_id`) REFERENCES `medicine` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `service_invoice` (
  `id` binary(16) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `due_date` date DEFAULT NULL,
  `total_fee` decimal(10,2) DEFAULT NULL,
  `medical_record_id` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_service_invoice_record` (`medical_record_id`),
  CONSTRAINT `FK_service_invoice_record` FOREIGN KEY (`medical_record_id`) REFERENCES `medical_record` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `service_item` (
  `service_id` binary(16) NOT NULL,
  `service_invoice_id` binary(16) NOT NULL,
  `number_service_use` int NOT NULL,
  PRIMARY KEY (`service_id`,`service_invoice_id`),
  KEY `FK_service_item_invoice` (`service_invoice_id`),
  CONSTRAINT `FK_service_item_service` FOREIGN KEY (`service_id`) REFERENCES `service` (`id`),
  CONSTRAINT `FK_service_item_invoice` FOREIGN KEY (`service_invoice_id`) REFERENCES `service_invoice` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `hospital_fee_payment` (
  `id` binary(16) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `due_date` date DEFAULT NULL,
  `is_paid` tinyint(1) NOT NULL,
  `total_fee` decimal(10,2) DEFAULT NULL,
  `cashier_counter_staff_id` binary(16) NOT NULL,
  `record_id` binary(16) NOT NULL,
  `patient_id` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_hospital_fee_cashier` (`cashier_counter_staff_id`),
  KEY `FK_hospital_fee_record` (`record_id`),
  KEY `FK_hospital_fee_patient` (`patient_id`),
  CONSTRAINT `FK_hospital_fee_cashier` FOREIGN KEY (`cashier_counter_staff_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FK_hospital_fee_record` FOREIGN KEY (`record_id`) REFERENCES `medical_record` (`id`),
  CONSTRAINT `FK_hospital_fee_patient` FOREIGN KEY (`patient_id`) REFERENCES `patient_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `notification` (
  `id` binary(16) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `is_read` tinyint(1) NOT NULL,
  `message` text,
  `payload` json DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `type` varchar(50) NOT NULL,
  `recipient_id` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_notification_recipient` (`recipient_id`),
  CONSTRAINT `FK_notification_recipient` FOREIGN KEY (`recipient_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `audit_log` (
  `id` binary(16) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `action` enum('DELETE','INSERT','UPDATE') NOT NULL,
  `changed_at` datetime(6) NOT NULL,
  `new_value` json DEFAULT NULL,
  `old_value` json DEFAULT NULL,
  `record_id` int NOT NULL,
  `table_name` varchar(50) NOT NULL,
  `changed_by` binary(16) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_audit_log_account` (`changed_by`),
  CONSTRAINT `FK_audit_log_account` FOREIGN KEY (`changed_by`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
