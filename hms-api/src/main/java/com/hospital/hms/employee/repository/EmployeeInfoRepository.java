package com.hospital.hms.employee.repository;

import com.hospital.hms.employee.entity.EmployeeInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeInfoRepository extends JpaRepository<EmployeeInfo, UUID>, JpaSpecificationExecutor<EmployeeInfo> {
    boolean existsByCode(String code);

    @Override
    @EntityGraph(attributePaths = {"account", "department"})
    Optional<EmployeeInfo> findById(UUID id);

    @EntityGraph(attributePaths = {"account", "department"})
    Optional<EmployeeInfo> findByAccount_Id(UUID id);

    @Override
    @EntityGraph(attributePaths = {"account", "department"})
    Page<EmployeeInfo> findAll(Specification<EmployeeInfo> spec, Pageable pageable);
}
