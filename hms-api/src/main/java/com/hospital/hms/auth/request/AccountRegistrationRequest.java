package com.hospital.hms.auth.request;

import com.hospital.hms.auth.entity.Role;
import com.hospital.hms.base.request.BaseRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountRegistrationRequest extends BaseRequest {
    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String password;

    private Role role;
}
