package com.hospital.hms.auth.service;

import com.hospital.hms.auth.response.AuthResponse;
import com.hospital.hms.auth.request.SignInRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.hospital.hms.exception.IdentityProviderException;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignInService {

    @Value("${app.keycloak.server-url}")
    private String serverUrl;

    @Value("${app.keycloak.realm}")
    private String realm;

    @Value("${app.keycloak.client-id}")
    private String clientId;

    @Value("${app.keycloak.client-secret}")
    private String clientSecret;

    private final WebClient webClient = WebClient.builder().build();

    public AuthResponse signInUser(SignInRequest request) {
        long startTime = System.currentTimeMillis();
        log.info("Processing SignInService.signInUser: {}", request.toLogString());

        String tokenUrl = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", clientId);
        if (clientSecret != null && !clientSecret.isEmpty()) {
            formData.add("client_secret", clientSecret);
        }
        formData.add("username", request.getUsername());
        formData.add("password", request.getPassword());

        log.debug("Prepared token exchange request to Keycloak for client: {}", clientId);

        try {
            // Suppressing generic Map cast warnings by using raw Map class provided by standard parser
            @SuppressWarnings("unchecked")
            Map<String, Object> response = webClient.post()
                    .uri(tokenUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            log.debug("Received response from Keycloak token endpoint");

            if (response != null) {
                long processingTime = System.currentTimeMillis() - startTime;
                log.info("Successfully authenticated user: {} in {}ms", request.getUsername(), processingTime);

                return AuthResponse.builder()
                        .accessToken((String) response.get("access_token"))
                        .refreshToken((String) response.get("refresh_token"))
                        .expiresIn(((Number) response.get("expires_in")).longValue())
                        .refreshExpiresIn(((Number) response.get("refresh_expires_in")).longValue())
                        .build();
            }
        } catch (org.springframework.web.reactive.function.client.WebClientResponseException e) {
            log.error("Authentication failed. Keycloak responded with {}: {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new IdentityProviderException("Keycloak Authentication Error: " + e.getResponseBodyAsString(), org.springframework.http.HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Error during authentication with Keycloak", e);
            throw new IdentityProviderException("Authentication failed", org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }

        throw new IdentityProviderException("Authentication failed", org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
