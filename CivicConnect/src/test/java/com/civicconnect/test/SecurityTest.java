package com.civicconnect.test;

import com.civicconnect.util.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Security & XSS Mitigation Test Suite")
public class SecurityTest {

    @Test
    @DisplayName("Sanitize Malicious XSS Script Injection Inputs")
    public void testXSSInputSanitization() {
        String scriptPayload = "<script>alert('XSS Attack');</script>";
        String sanitized = SecurityUtil.sanitizeInput(scriptPayload);

        assertNotNull(sanitized);
        assertFalse(sanitized.contains("<script>"), "Raw script tags must be sanitized");
        assertTrue(sanitized.contains("&lt;script&gt;"), "Script brackets must be HTML entity encoded");
    }

    @Test
    @DisplayName("Sanitize HTML Event Handler Injection Attributes")
    public void testXSSEventHandlerSanitization() {
        String eventPayload = "<img src='x' onerror='alert(\"XSS\")'>";
        String sanitized = SecurityUtil.sanitizeInput(eventPayload);

        assertFalse(sanitized.contains("<img"));
        assertTrue(sanitized.contains("&lt;img"));
    }

    @Test
    @DisplayName("Validate Session ID Format Integrity")
    public void testSessionIdIntegrity() {
        assertTrue(SecurityUtil.isValidSessionId("ABC1234567890XYZ"));
        assertFalse(SecurityUtil.isValidSessionId("short"));
        assertFalse(SecurityUtil.isValidSessionId("<invalid_script>"));
    }
}
