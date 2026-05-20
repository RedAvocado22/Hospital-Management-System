package com.hospital.hms.auth.service;

import com.hospital.hms.auth.request.LogoutRequest;
import com.hospital.hms.base.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class LogoutService extends BaseService<LogoutRequest, Void> {

    private final TokenBlacklistService tokenBlacklistService;

    @Override
    protected Void doProcess(LogoutRequest request) {
        JwtAuthenticationToken jwtAuthentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = jwtAuthentication.getToken();
        String jti = jwt.getId();
        Instant exp = jwt.getExpiresAt();

        long ttl = exp.getEpochSecond() - Instant.now().getEpochSecond();
        tokenBlacklistService.blacklist(jti, ttl);
        return null;
    }
}
