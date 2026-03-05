package com.hospital.hms.auth.request;

import com.hospital.hms.base.request.BaseRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccountRegistrationRequest extends BaseRequest {
    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String password;

    private String role;
}
