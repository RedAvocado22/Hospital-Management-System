package com.hospital.hms.patient.request;

import com.hospital.hms.base.request.BaseRequest;
import com.hospital.hms.common.enums.Gender;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdatePatientRequest extends BaseRequest {

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    private String email;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private Gender gender;
    private String address;
    private String phone;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
