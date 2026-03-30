package com.hospital.hms.employee.service;

import com.hospital.hms.employee.dto.EmployeeSummary;
import com.hospital.hms.employee.entity.EmployeeInfo;
import com.hospital.hms.employee.repository.EmployeeInfoRepository;
import com.hospital.hms.employee.response.EmployeeDetailResponse;
import com.hospital.hms.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeQueryService {

    private final EmployeeInfoRepository employeeInfoRepository;

    @Transactional(readOnly = true)
    public EmployeeDetailResponse getByAccountId(UUID id) {
        EmployeeInfo ei = employeeInfoRepository.findByAccount_Id(id).orElseThrow(
                () -> new NotFoundException("Employee info with id: " + id + " not found")
        );

        return EmployeeDetailResponse.from(ei);
    }

    @Transactional(readOnly = true)
    public EmployeeSummary getInfoByAccountId(UUID id) {
        log.debug("Fetching employee info for accountId: {}", id);

        EmployeeInfo ei = employeeInfoRepository.findByAccount_Id(id).orElseThrow(
                () -> new NotFoundException("Employee info with id: " + id + " not found")
        );

        return EmployeeSummary.from(ei);
    }
}
