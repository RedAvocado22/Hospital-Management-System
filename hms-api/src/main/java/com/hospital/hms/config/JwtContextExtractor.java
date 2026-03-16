package com.hospital.hms.config;

import com.hospital.hms.base.UserContext;
import com.hospital.hms.exception.IdentityProviderException;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class JwtContextExtractor {

    public static UserContext extractUserContext(Jwt jwt) {
        Object sub = jwt.getClaims().get("sub");
        Object username = jwt.getClaims().get("preferred_username");
        Object email = jwt.getClaims().get("email");

        Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");

        if (realmAccess == null || realmAccess.isEmpty()) {
            throw new IdentityProviderException("Missing real_access in JWT.");
        }

        Collection<String> roles = (Collection<String>) realmAccess.get("roles");

        if (roles == null || roles.isEmpty()) {
            throw new IdentityProviderException("Missing roles in JWT.");
        }

        return UserContext.builder()
                .userId(UUID.fromString((String) sub))
                .username((String) username)
                .email((String) email)
                .roles(roles.stream().map(role -> "ROLE_" + role.toUpperCase()).collect(Collectors.toSet()))
                .build();
    }
}
