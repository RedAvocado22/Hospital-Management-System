package com.hospital.hms.auth.repository;

import com.hospital.hms.auth.entity.EmployeeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeInfoRepository extends JpaRepository<EmployeeInfo, UUID> {

    Optional<EmployeeInfo> findByAccount_Id(UUID accountId);
}
