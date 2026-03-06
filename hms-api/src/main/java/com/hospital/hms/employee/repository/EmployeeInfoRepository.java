package com.hospital.hms.employee.repository;

import com.hospital.hms.employee.entity.EmployeeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface EmployeeInfoRepository extends JpaRepository<EmployeeInfo, UUID>, JpaSpecificationExecutor<EmployeeInfo> {
    boolean existsByCode(String code);
}
