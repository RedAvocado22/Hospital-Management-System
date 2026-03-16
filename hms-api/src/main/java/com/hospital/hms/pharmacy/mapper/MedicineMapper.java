package com.hospital.hms.pharmacy.mapper;

import com.hospital.hms.pharmacy.entity.Medicine;
import com.hospital.hms.pharmacy.request.CreateMedicineRequest;
import com.hospital.hms.pharmacy.response.MedicineResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MedicineMapper {

    @Mapping(source = "medicine.id", target = "id")
    MedicineResponse toResponse(Medicine medicine);

    @Mapping(target = "id", ignore = true)
    Medicine toEntity(CreateMedicineRequest createRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget Medicine entity, CreateMedicineRequest request);
}
