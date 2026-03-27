package com.hospital.hms.employee.service;

import com.hospital.hms.employee.entity.EmployeeInfo;
import com.hospital.hms.employee.repository.EmployeeInfoRepository;
import com.hospital.hms.employee.response.EmployeeResponse;
import com.hospital.hms.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeQueryService {

    private final EmployeeInfoRepository employeeInfoRepository;

    @Transactional(readOnly = true)
    public EmployeeResponse getByAccountId(UUID id) {
        EmployeeInfo ei = employeeInfoRepository.findByAccount_Id(id).orElseThrow(
                () -> new NotFoundException("Employee info with id: " + id + " not found")
        );

        return EmployeeResponse.from(ei);
    }
}
