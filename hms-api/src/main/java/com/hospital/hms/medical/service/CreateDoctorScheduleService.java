package com.hospital.hms.medical.service;

import com.hospital.hms.auth.service.AccountQueryService;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.exception.BusinessException;
import com.hospital.hms.medical.entity.DoctorSchedule;
import com.hospital.hms.medical.repository.DoctorScheduleRepository;
import com.hospital.hms.medical.request.CreateDoctorScheduleRequest;
import com.hospital.hms.medical.response.DoctorScheduleDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateDoctorScheduleService extends BaseService<CreateDoctorScheduleRequest, DoctorScheduleDetailResponse> {

    private final DoctorScheduleRepository doctorScheduleRepository;
    private final AccountQueryService accountQueryService;


    @Override
    @Transactional
    public DoctorScheduleDetailResponse execute(CreateDoctorScheduleRequest request) {
        return super.execute(request);
    }

    @Override
    protected DoctorScheduleDetailResponse doProcess(CreateDoctorScheduleRequest request) {

        DoctorSchedule ds = DoctorSchedule.builder()
                .doctor(accountQueryService.getReferenceById(request.getDoctorId()))
                .type(request.getType())
                .date(request.getDate())
                .maxPatients(request.getMaxPatients())
                .isAvailable(true)
                .build();

        DoctorSchedule saved = doctorScheduleRepository.save(ds);

        return DoctorScheduleDetailResponse.from(saved);
    }

    @Override
    protected void validate(CreateDoctorScheduleRequest request) {
        super.validate(request);

        if (!accountQueryService.hasRole(request.getDoctorId(), "doctor")) {
            throw new BusinessException("Account is not a doctor");
        }

        if (doctorScheduleRepository.existsByDoctor_IdAndDateAndType(request.getDoctorId(), request.getDate(), request.getType())) {
            throw new BusinessException("Doctor already has this schedule");
        }
    }
}
