package com.hospital.hms.pharmacy.mapper;

import com.hospital.hms.pharmacy.dto.request.MedicineCreateRequest;
import com.hospital.hms.pharmacy.dto.response.MedicineResponse;
import com.hospital.hms.pharmacy.entity.Medicine;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MedicineMapper {

    @Mapping(source = "medicine.id", target = "id")
    MedicineResponse toResponse(Medicine medicine);

    @Mapping(target = "id", ignore = true)
    Medicine toEntity(MedicineCreateRequest createRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget Medicine entity, MedicineCreateRequest request);
}
