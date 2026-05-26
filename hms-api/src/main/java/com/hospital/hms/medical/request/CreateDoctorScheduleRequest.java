package com.hospital.hms.medical.request;

import com.hospital.hms.base.request.BaseRequest;
import com.hospital.hms.common.enums.ShiftType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class CreateDoctorScheduleRequest extends BaseRequest {
    @NotNull
    private UUID doctorId;

    @NotNull
    private LocalDate date;

    @NotNull
    private ShiftType type;

    @NotNull
    @Min(5)
    @Max(30)
    private Integer maxPatients;

}
