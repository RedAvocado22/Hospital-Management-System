package com.hospital.hms.exam_queue.mapper;

import com.hospital.hms.exam_queue.entity.QueueInfo;
import com.hospital.hms.exam_queue.request.AddToQueueRequest;
import com.hospital.hms.exam_queue.response.QueueInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QueueInfoMapper {

    QueueInfoResponse toResponse(QueueInfo queueInfo);

    @Mapping(target = "id", ignore = true)
    QueueInfo toEntity(AddToQueueRequest createRequest);

}
