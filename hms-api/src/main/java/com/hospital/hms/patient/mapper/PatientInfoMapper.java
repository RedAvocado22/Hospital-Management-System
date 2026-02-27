package com.hospital.hms.patient.mapper;

import com.hospital.hms.patient.dto.request.PatientCreateRequest;
import com.hospital.hms.patient.dto.response.PatientResponse;
import com.hospital.hms.patient.entity.PatientInfo;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PatientInfoMapper {

    @Mapping(source = "account.id", target = "accountId")
    PatientResponse toResponse(PatientInfo patientInfo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true)
    PatientInfo toEntity(PatientCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "account", ignore = true)
    void update(@MappingTarget PatientInfo entity, PatientCreateRequest request);
}