package com.hospital.hms.exam_queue.service;

import com.hospital.hms.base.response.PaginatedResponse;
import com.hospital.hms.base.service.BaseService;
import com.hospital.hms.exam_queue.entity.QueueInfo;
import com.hospital.hms.exam_queue.mapper.QueueInfoMapper;
import com.hospital.hms.exam_queue.repository.QueueRepository;
import com.hospital.hms.exam_queue.request.GetExamQueueRequest;
import com.hospital.hms.exam_queue.response.QueueInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetTodayQueueService extends BaseService<GetExamQueueRequest, PaginatedResponse<QueueInfoResponse>> {

    private final QueueInfoMapper queueInfoMapper;
    private final QueueRepository queueRepository;

    @Override
    protected PaginatedResponse<QueueInfoResponse> doProcess(GetExamQueueRequest request) {
        if (request.getUserContext() == null) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }
        log.debug("Fetching today's exam-queue with pagination - page: {}, size: {}", request.getPage(), request.getSize());

        Pageable pageable = request.toPageable();
        Page<QueueInfo> queueInfoPage = queueRepository.findByDate(LocalDate.now(), pageable);

        if (queueInfoPage.isEmpty()) {
            log.debug("No today's exam-queue found for given filters");
        }

        log.info("Found {} today's exam-queue on page {} of {}", queueInfoPage.getNumberOfElements(), queueInfoPage.getNumber(), queueInfoPage.getTotalPages());

        Page<QueueInfoResponse> responsePage = queueInfoPage.map(queueInfoMapper::toResponse);
        return PaginatedResponse.from(responsePage);
    }
}
