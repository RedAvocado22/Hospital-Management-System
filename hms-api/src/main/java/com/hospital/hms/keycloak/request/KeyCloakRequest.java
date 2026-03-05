package com.hospital.hms.keycloak.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class KeyCloakRequest {
    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String password;
}
