package com.kglsys.infra.jwt;

import org.springframework.security.core.Authentication;

public interface JwtTokenGenerator {
    String generateToken(Authentication authentication);
    String getUsernameFromToken(String token);
    boolean validateToken(String authToken);
}
