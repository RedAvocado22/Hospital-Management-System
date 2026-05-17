package com.hospital.hms.exam_queue.service;

import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.exam_queue.entity.QueueInfo;
import com.hospital.hms.exam_queue.entity.QueueStatus;
import com.hospital.hms.exam_queue.mapper.QueueInfoMapper;
import com.hospital.hms.exam_queue.repository.QueueRepository;
import com.hospital.hms.exam_queue.request.UpdateQueueRequest;
import com.hospital.hms.exam_queue.response.QueueInfoResponse;
import com.hospital.hms.exception.BusinessException;
import com.hospital.hms.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateQueueService extends BaseService<UpdateQueueRequest, QueueInfoResponse> {
    private final QueueRepository queueRepository;
    private final QueueInfoMapper queueInfoMapper;

    @Override
    protected QueueInfoResponse doProcess(UpdateQueueRequest request) {
        if (request.getUserContext() == null) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }

        log.info("Updating queue id: {} by user id: {}",
                request.getId(), request.getUserContext().getUserId());

        QueueInfo response = queueRepository.findById(request.getId()).orElseThrow(
                () -> new NotFoundException("Your queue can't found")
        );

        if (response.getStatus() == QueueStatus.CANCELLED || response.getStatus() == QueueStatus.DONE) {
            throw new BusinessException("Cannot update a completed or cancelled  entry");
        }
        response.setStatus(request.getStatus());
        QueueInfo saved = queueRepository.save(response);

        log.info("Queue status {} updated successfully", saved.getId());


        return queueInfoMapper.toResponse(saved);
    }
}
