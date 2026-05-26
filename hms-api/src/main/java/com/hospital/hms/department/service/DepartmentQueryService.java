package com.hospital.hms.department.service;

import com.hospital.hms.department.dto.DepartmentInfo;
import com.hospital.hms.department.entity.Department;
import com.hospital.hms.department.repository.DepartmentRepository;
import com.hospital.hms.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartmentQueryService {

    private final DepartmentRepository departmentRepository;

    public DepartmentInfo getDepartmentIdByName(String name) {
        return DepartmentInfo.from(
                departmentRepository.findByNameIgnoreCase(name).orElseThrow(
                        () -> new NotFoundException("Department not found")
                )
        );
    }

    public Department getReferenceById(UUID id) {
        return departmentRepository.getReferenceById(id);
    }
}
