package com.hospital.hms.exam_queue.service;

import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.exam_queue.entity.QueueInfo;
import com.hospital.hms.exam_queue.entity.QueueStatus;
import com.hospital.hms.exam_queue.mapper.QueueInfoMapper;
import com.hospital.hms.exam_queue.repository.QueueRepository;
import com.hospital.hms.exam_queue.request.AddToQueueRequest;
import com.hospital.hms.exam_queue.response.QueueInfoResponse;
import com.hospital.hms.exception.BusinessException;
import com.hospital.hms.patient.service.PatientQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddToQueueService extends BaseService<AddToQueueRequest, QueueInfoResponse> {

    private final QueueRepository queueRepository;
    private final PatientQueryService patientQueryService;
    private final QueueInfoMapper queueInfoMapper;

    @Override
    protected QueueInfoResponse doProcess(AddToQueueRequest request) {
//        if (request.getUserContext() == null) {
//            throw new AccessDeniedException("You do not have permission to access this resource");
//        }
        patientQueryService.getReferenceById(request.getPatientId());
        log.info("Creating Exam queue  — patientId: {}",
                request.getPatientId());
        if (queueRepository.existsByDateAndPatientId(LocalDate.now(), request.getPatientId())) {
            throw new BusinessException("Patient is already in today's queue");
        }
        Integer position = queueRepository.countByDate(LocalDate.now()) + 1;
        QueueInfo queueInfo = QueueInfo.builder()
                .patientId(request.getPatientId())
                .date(LocalDate.now())
                .position(position)
                .status(QueueStatus.WAITING)
                .build();

        QueueInfo saved = queueRepository.save(queueInfo);

        log.info("Medical record created, id: {}", saved.getId());

        return queueInfoMapper.toResponse(saved);
    }
}
