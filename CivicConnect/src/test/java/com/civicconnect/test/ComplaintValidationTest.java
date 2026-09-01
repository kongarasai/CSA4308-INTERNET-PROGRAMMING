package com.civicconnect.test;

import com.civicconnect.util.SecurityUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CO2 Client/Server Regex Validation Test Suite")
public class ComplaintValidationTest {

    @Test
    @DisplayName("Test Valid Indian Mobile Numbers (10 Digits starting with 6-9)")
    public void testValidMobileNumbers() {
        assertTrue(SecurityUtil.isValidMobile("9876543210"));
        assertTrue(SecurityUtil.isValidMobile("8123456789"));
        assertTrue(SecurityUtil.isValidMobile("7012345678"));
        assertTrue(SecurityUtil.isValidMobile("6999999999"));
    }

    @Test
    @DisplayName("Test Invalid Mobile Numbers")
    public void testInvalidMobileNumbers() {
        assertFalse(SecurityUtil.isValidMobile("5876543210")); // Invalid prefix 5
        assertFalse(SecurityUtil.isValidMobile("987654321"));  // 9 digits
        assertFalse(SecurityUtil.isValidMobile("98765432100")); // 11 digits
        assertFalse(SecurityUtil.isValidMobile("ABCDEFGHIJ")); // Alphabetic
        assertFalse(SecurityUtil.isValidMobile(""));          // Empty string
        assertFalse(SecurityUtil.isValidMobile(null));        // Null input
    }

    @Test
    @DisplayName("Test Valid Indian PIN Codes (6 Digits)")
    public void testValidPincodes() {
        assertTrue(SecurityUtil.isValidPincode("560001"));
        assertTrue(SecurityUtil.isValidPincode("110001"));
        assertTrue(SecurityUtil.isValidPincode("400001"));
    }

    @Test
    @DisplayName("Test Invalid PIN Codes")
    public void testInvalidPincodes() {
        assertFalse(SecurityUtil.isValidPincode("011001")); // Cannot start with 0
        assertFalse(SecurityUtil.isValidPincode("56000"));  // 5 digits
        assertFalse(SecurityUtil.isValidPincode("5600011")); // 7 digits
        assertFalse(SecurityUtil.isValidPincode("56000A")); // Non-numeric
    }
}
