package com.hospital.hms.employee.repository;

import com.hospital.hms.employee.entity.EmployeeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeInfoRepository extends JpaRepository<EmployeeInfo, UUID> {
    boolean existsByCode(String code);
}
