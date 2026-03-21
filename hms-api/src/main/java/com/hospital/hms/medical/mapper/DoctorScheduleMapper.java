package com.hospital.hms.medical.mapper;

import com.hospital.hms.medical.entity.DoctorSchedule;
import com.hospital.hms.medical.response.DoctorScheduleResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorScheduleMapper {

    DoctorScheduleResponse toResponse(DoctorSchedule doctorSchedule);

}
