package com.housing_management.api.common.util;

import com.housing_management.api.common.exception.UserNotFoundException;
import com.housing_management.api.config.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    private SecurityUtils() {}

    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getId();
        }

        throw new UserNotFoundException("User tidak terautentikasi atau session tidak valid");
    }
}
