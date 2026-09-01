package com.civicconnect.test;

import com.civicconnect.soap.DepartmentAllocationResponse;
import com.civicconnect.soap.DepartmentAllocationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CO5 SOAP DepartmentAllocationService Test Suite")
public class SOAPServiceTest {

    private DepartmentAllocationServiceImpl soapService;

    @BeforeEach
    public void setUp() {
        this.soapService = new DepartmentAllocationServiceImpl();
    }

    @Test
    @DisplayName("Route POTHOLE Category to Roads & Infrastructure Department with 24h SLA")
    public void testPotholeAllocation() {
        DepartmentAllocationResponse res = soapService.allocateDepartment("POTHOLE", "MG Road Ward 12");
        assertNotNull(res);
        assertEquals("Roads & Infrastructure", res.getDepartment());
        assertEquals("24 Hours", res.getSlaHours());
        assertEquals("HIGH", res.getPriority());
    }

    @Test
    @DisplayName("Route GARBAGE_OVERFLOW Category to Sanitation Department with 12h SLA")
    public void testGarbageAllocation() {
        DepartmentAllocationResponse res = soapService.allocateDepartment("GARBAGE_OVERFLOW", "Market Road Ward 4");
        assertNotNull(res);
        assertEquals("Sanitation & Waste Management", res.getDepartment());
        assertEquals("12 Hours", res.getSlaHours());
    }

    @Test
    @DisplayName("Route BROKEN_STREETLIGHT Category to Electricity Department with 36h SLA")
    public void testStreetlightAllocation() {
        DepartmentAllocationResponse res = soapService.allocateDepartment("BROKEN_STREETLIGHT", "7th Cross Indiranagar");
        assertNotNull(res);
        assertEquals("Electricity & Street Lighting", res.getDepartment());
        assertEquals("36 Hours", res.getSlaHours());
    }

    @Test
    @DisplayName("Route WATER_LEAKAGE Category to Water Supply Board with 18h SLA")
    public void testWaterLeakageAllocation() {
        DepartmentAllocationResponse res = soapService.allocateDepartment("WATER_LEAKAGE", "4th Block Koramangala");
        assertNotNull(res);
        assertEquals("Water Supply & Sewerage Board", res.getDepartment());
        assertEquals("18 Hours", res.getSlaHours());
        assertEquals("CRITICAL", res.getPriority());
    }
}
