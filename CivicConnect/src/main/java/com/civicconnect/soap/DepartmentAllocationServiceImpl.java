package com.civicconnect.soap;

import javax.jws.WebService;

/**
 * Service Implementation Bean (SIB) for DepartmentAllocationService SOAP Web Service.
 * Implements automated department routing logic based on category and location.
 */
@WebService(
    endpointInterface = "com.civicconnect.soap.DepartmentAllocationService",
    serviceName = "DepartmentAllocationService",
    portName = "DepartmentAllocationPort",
    targetNamespace = "http://soap.civicconnect.com/"
)
public class DepartmentAllocationServiceImpl implements DepartmentAllocationService {

    @Override
    public DepartmentAllocationResponse allocateDepartment(String category, String location) {
        if (category == null || category.trim().isEmpty()) {
            category = "GENERAL";
        }
        
        String cleanCategory = category.trim().toUpperCase();
        String department;
        String sla;
        String priority;

        switch (cleanCategory) {
            case "POTHOLE":
            case "ROADS":
                department = "Roads & Infrastructure";
                sla = "24 Hours";
                priority = "HIGH";
                break;
            case "GARBAGE_OVERFLOW":
            case "GARBAGE":
            case "SANITATION":
                department = "Sanitation & Waste Management";
                sla = "12 Hours";
                priority = "HIGH";
                break;
            case "BROKEN_STREETLIGHT":
            case "STREETLIGHT":
            case "ELECTRICITY":
                department = "Electricity & Street Lighting";
                sla = "36 Hours";
                priority = "MEDIUM";
                break;
            case "WATER_LEAKAGE":
            case "WATER":
                department = "Water Supply & Sewerage Board";
                sla = "18 Hours";
                priority = "CRITICAL";
                break;
            default:
                department = "General Municipal Administration";
                sla = "48 Hours";
                priority = "LOW";
                break;
        }

        return new DepartmentAllocationResponse(category, location, department, sla, priority, "SUCCESS");
    }
}
