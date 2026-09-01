package com.civicconnect.util;

import java.util.regex.Pattern;

/**
 * Security Utility providing input sanitization (XSS defense) and server-side pattern validations.
 */
public class SecurityUtil {

    private static final Pattern INDIAN_MOBILE_PATTERN = Pattern.compile("^[6-9]\\d{9}$");
    private static final Pattern INDIAN_PINCODE_PATTERN = Pattern.compile("^[1-9][0-9]{5}$");

    /**
     * Sanitizes raw HTML input to prevent Cross-Site Scripting (XSS) attacks.
     */
    public static String sanitizeInput(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#x27;")
                    .replace("/", "&#x2F;");
    }

    /**
     * Validates Indian 10-digit mobile number format.
     */
    public static boolean isValidMobile(String mobile) {
        if (mobile == null) return false;
        return INDIAN_MOBILE_PATTERN.matcher(mobile.trim()).matches();
    }

    /**
     * Validates Indian 6-digit PIN code format.
     */
    public static boolean isValidPincode(String pincode) {
        if (pincode == null) return false;
        return INDIAN_PINCODE_PATTERN.matcher(pincode.trim()).matches();
    }

    /**
     * Sanitizes user session tokens / cookie inputs.
     */
    public static boolean isValidSessionId(String sessionId) {
        if (sessionId == null || sessionId.trim().isEmpty()) return false;
        return sessionId.matches("^[a-zA-Z0-9_-]{8,128}$");
    }
}
