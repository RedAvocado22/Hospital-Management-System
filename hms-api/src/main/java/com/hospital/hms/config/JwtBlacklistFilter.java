package com.hospital.hms.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.hms.auth.service.TokenBlacklistService;
import com.hospital.hms.base.api.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtBlacklistFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;
    private final TokenBlacklistService tokenBlacklistService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        try {
            Jwt jwt = jwtDecoder.decode(token);
            String jti = jwt.getId();
            if (tokenBlacklistService.isBlacklisted(jti)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                ApiResponse<Void> body = ApiResponse.simpleError("Token has been revoked", HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(objectMapper.writeValueAsString(body));
                return;
            }
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
