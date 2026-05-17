package com.hospital.hms.exam_queue.service;

import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.exam_queue.entity.QueueInfo;
import com.hospital.hms.exam_queue.entity.QueueStatus;
import com.hospital.hms.exam_queue.repository.QueueRepository;
import com.hospital.hms.exam_queue.request.DeleteQueueRequest;
import com.hospital.hms.exception.BusinessException;
import com.hospital.hms.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteQueueService extends BaseService<DeleteQueueRequest, Void> {
    private final QueueRepository queueRepository;

    @Override
    protected Void doProcess(DeleteQueueRequest request) {
        if (request.getUserContext() == null) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }

        log.info("Deleting queue id: {} by user id: {}",
                request.getId(), request.getUserContext().getUserId());

        QueueInfo queue = queueRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Queue not found"));

        if (queue.getStatus() == QueueStatus.DONE) {
            throw new BusinessException("Cannot delete a completed entry");
        }

        queueRepository.delete(queue);

        return null;
    }
}
