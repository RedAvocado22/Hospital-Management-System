package com.hospital.hms.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final StringRedisTemplate template;

    public void blacklist(String jti, long ttl) {
        template.opsForValue().set(jti, "blacklisted", ttl, TimeUnit.SECONDS);
    }

    public boolean isBlacklisted(String jti) {
        return Boolean.TRUE.equals(template.opsForSet().getOperations().hasKey(jti));
    }
}
