package com.hospital.hms.exam_queue.controller;

import com.hospital.hms.base.api.ApiResponse;
import com.hospital.hms.base.api.ResponseMetadata;
import com.hospital.hms.base.response.PaginatedResponse;
import com.hospital.hms.exam_queue.request.AddToQueueRequest;
import com.hospital.hms.exam_queue.request.DeleteQueueRequest;
import com.hospital.hms.exam_queue.request.GetExamQueueRequest;
import com.hospital.hms.exam_queue.request.UpdateQueueRequest;
import com.hospital.hms.exam_queue.response.QueueInfoResponse;
import com.hospital.hms.exam_queue.service.AddToQueueService;
import com.hospital.hms.exam_queue.service.DeleteQueueService;
import com.hospital.hms.exam_queue.service.GetTodayQueueService;
import com.hospital.hms.exam_queue.service.UpdateQueueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/v1/exam-queue")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
public class ExamQueueController {
    private final GetTodayQueueService getTodayQueueService;
    private final AddToQueueService addToQueueService;
    private final UpdateQueueService updateQueueService;
    private final DeleteQueueService deleteQueueService;


    @GetMapping()
    public ResponseEntity<ApiResponse<PaginatedResponse<QueueInfoResponse>>> getTodayQueue(@ModelAttribute GetExamQueueRequest request, HttpServletRequest httpRequest) {
        long startTime = System.currentTimeMillis();
        String traceId = java.util.UUID.randomUUID().toString();

        log.info("[TraceID: {}]", traceId);

        PaginatedResponse<QueueInfoResponse> response = getTodayQueueService.execute(request);

        long duration = System.currentTimeMillis() - startTime;

        ResponseMetadata metadata = ResponseMetadata.builder()
                .traceId(traceId)
                .path(httpRequest.getRequestURI())
                .method(httpRequest.getMethod())
                .duration(duration)
                .apiVersion("v1")
                .build();

        ApiResponse<PaginatedResponse<QueueInfoResponse>> apiResponse = ApiResponse.success(
                response,
                "Get Today's Queue successfully",
                HttpStatus.OK.value(),
                metadata
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<QueueInfoResponse>> addPatientToQueue(@Valid @RequestBody AddToQueueRequest request, HttpServletRequest httpRequest) {

        long startTime = System.currentTimeMillis();
        String traceId = java.util.UUID.randomUUID().toString();

        log.info("[TraceID: {}] Adding new patient to queue with id: {}", traceId, request.getPatientId());

        QueueInfoResponse response = addToQueueService.execute(request);

        long duration = System.currentTimeMillis() - startTime;

        ResponseMetadata metadata = ResponseMetadata.builder()
                .traceId(traceId)
                .path(httpRequest.getRequestURI())
                .method(httpRequest.getMethod())
                .duration(duration)
                .apiVersion("v1")
                .build();

        ApiResponse<QueueInfoResponse> apiResponse = ApiResponse.success(
                response,
                "Patient added successfully",
                HttpStatus.CREATED.value(),
                metadata
        );

        log.info("[TraceID: {}] Patient added successfully with ID: {} (took {}ms)",
                traceId, response.id(), duration);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<QueueInfoResponse>> updateMedicine(@Valid @PathVariable UUID id, @RequestBody UpdateQueueRequest request, HttpServletRequest httpRequest) {
        long startTime = System.currentTimeMillis();
        String traceId = java.util.UUID.randomUUID().toString();
        log.info("[TraceID: {}] Updating queue status with id: {}", traceId, request.getId());

        request.setId(id);
        QueueInfoResponse response = updateQueueService.execute(request);

        long duration = System.currentTimeMillis() - startTime;

        ResponseMetadata metadata = ResponseMetadata.builder()
                .traceId(traceId)
                .path(httpRequest.getRequestURI())
                .method(httpRequest.getMethod())
                .duration(duration)
                .apiVersion("v1")
                .build();

        ApiResponse<QueueInfoResponse> apiResponse = ApiResponse.success(
                response,
                "Status of queue updated successfully",
                HttpStatus.OK.value(),
                metadata
        );

        log.info("[TraceID: {}] Updated queue id: {} successfully (took {}ms)",
                traceId, response.id(), duration);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteQueue(@PathVariable UUID id, HttpServletRequest httpRequest) {
        long startTime = System.currentTimeMillis();
        String traceId = java.util.UUID.randomUUID().toString();

        DeleteQueueRequest request = new DeleteQueueRequest();
        request.setId(id);
        log.info("[TraceID: {}] Delete queue with id: {}", traceId, request.getId());

        deleteQueueService.execute(request);

        long duration = System.currentTimeMillis() - startTime;

        ResponseMetadata metadata = ResponseMetadata.builder()
                .traceId(traceId)
                .path(httpRequest.getRequestURI())
                .method(httpRequest.getMethod())
                .duration(duration)
                .apiVersion("v1")
                .build();

        ApiResponse<Void> apiResponse = ApiResponse.success(
                null,
                "Queue deleted successfully",
                HttpStatus.NO_CONTENT.value(),
                metadata
        );

        log.info("[TraceID: {}] successfully (took {}ms)",
                traceId, duration);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(apiResponse);
    }
}
