package com.hospital.hms.exam_queue.service;

import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.exam_queue.entity.QueueInfo;
import com.hospital.hms.exam_queue.entity.QueueStatus;
import com.hospital.hms.exam_queue.mapper.QueueInfoMapper;
import com.hospital.hms.exam_queue.repository.QueueRepository;
import com.hospital.hms.exam_queue.request.AddToQueueRequest;
import com.hospital.hms.exam_queue.response.QueueInfoResponse;
import com.hospital.hms.patient.service.PatientQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddToQueueService extends BaseService<AddToQueueRequest, QueueInfoResponse> {

    private final QueueRepository queueRepository;
    private final PatientQueryService patientQueryService;
    private final QueueInfoMapper queueInfoMapper;

    @Override
    @Transactional
    protected QueueInfoResponse doProcess(AddToQueueRequest request) {
        patientQueryService.getReferenceById(request.getPatient().getId());
        log.info("Creating Exam queue  — patientId: {}",
                request.getPatient().getId());
        LocalDate currentDate = LocalDate.now();
        Integer count = queueRepository.countByDateWithLock(currentDate);
        Integer position = count + 1;
        QueueInfo queueInfo = QueueInfo.builder()
                .patient(request.getPatient())
                .date(currentDate)
                .position(position)
                .status(QueueStatus.WAITING)
                .build();

        QueueInfo saved = queueRepository.save(queueInfo);

        log.info("New queue created, id: {}", saved.getId());

        return queueInfoMapper.toResponse(saved);
    }
}
