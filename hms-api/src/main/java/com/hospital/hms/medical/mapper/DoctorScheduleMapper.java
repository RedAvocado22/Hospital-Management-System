package com.hospital.hms.medical.mapper;

import com.hospital.hms.medical.entity.DoctorSchedule;
import com.hospital.hms.medical.response.DoctorScheduleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface DoctorScheduleMapper {

    @Mappings({
            @Mapping(source = "doctor.id", target = "doctorId"),
            @Mapping(source = "doctor.fullName", target = "fullName")
    })
    DoctorScheduleResponse toResponse(DoctorSchedule doctorSchedule);

}
