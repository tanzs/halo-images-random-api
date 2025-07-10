package com.aiym.haloapi.util;

import com.aiym.haloapi.properties.RefererProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * @author Tanzs
 * @date 2025/7/10 下午12:20
 * @description
 */
@Component
public class RefererValidator {
    private final RefererProperties refererProperties;

    public RefererValidator(RefererProperties refererProperties) {
        this.refererProperties = refererProperties;
    }

    public boolean isValid(HttpServletRequest request) {
        if (refererProperties.isAllowAll()) {
            return true;
        }

        String referer = request.getHeader("Referer");
        if (referer == null) {
            return false;
        }

        return refererProperties.getAllowList().stream().anyMatch(referer::contains);
    }
}
