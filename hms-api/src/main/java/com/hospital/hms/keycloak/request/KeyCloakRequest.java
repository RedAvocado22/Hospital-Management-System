package com.hospital.hms.keycloak.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class KeyCloakRequest {
    String username;

    String email;

    String firstName;

    String lastName;

    String password;
}
