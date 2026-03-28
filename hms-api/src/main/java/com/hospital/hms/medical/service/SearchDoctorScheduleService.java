package com.hospital.hms.medical.service;

import com.hospital.hms.auth.repository.AccountRepository;
import com.hospital.hms.base.response.PaginatedResponse;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.medical.entity.DoctorSchedule;
import com.hospital.hms.medical.mapper.DoctorScheduleMapper;
import com.hospital.hms.medical.repository.DoctorScheduleRepository;
import com.hospital.hms.medical.request.SearchDoctorScheduleRequest;
import com.hospital.hms.medical.response.DoctorScheduleDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchDoctorScheduleService extends BaseService<SearchDoctorScheduleRequest, PaginatedResponse<DoctorScheduleDetailResponse>> {
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final AccountRepository accountRepository;
    private final DoctorScheduleMapper doctorScheduleMapper;

    @Override
    @Transactional(readOnly = true)
    protected PaginatedResponse<DoctorScheduleDetailResponse> doProcess(SearchDoctorScheduleRequest request) {
        log.debug("Search doctor schedule request received");

        Page<DoctorSchedule> doctorSchedulePage;
        Pageable pageable = request.toPageable();
        LocalDate date = request.getDate() != null ? request.getDate() : LocalDate.now();
        if (request.getDoctorId() == null) {
            doctorSchedulePage = doctorScheduleRepository.findByDate(date, pageable);
        } else {
            if (!accountRepository.existsByIdAndRoleName(request.getDoctorId(), "doctor")) {
                throw new NotFoundException("Doctor does not exist in the system");
            }
            doctorSchedulePage = doctorScheduleRepository.findByDoctor_IdAndDate(request.getDoctorId(), date, pageable);
        }
        Page<DoctorScheduleDetailResponse> responses = doctorSchedulePage.map(doctorScheduleMapper::toResponse);
        return PaginatedResponse.from(responses);
    }
}
