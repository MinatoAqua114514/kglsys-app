package com.kglsys.application.util;

import com.kglsys.infra.details.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityUtil {

    public static Optional<CustomUserDetails> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.empty();
        }
        return Optional.of((CustomUserDetails) authentication.getPrincipal());
    }

    public static Optional<Long> getCurrentUserId() {
        return getCurrentUser().map(CustomUserDetails::getId);
    }
}