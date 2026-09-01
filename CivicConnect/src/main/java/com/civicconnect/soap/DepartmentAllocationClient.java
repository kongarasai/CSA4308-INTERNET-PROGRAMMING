package com.civicconnect.soap;

import com.civicconnect.model.DepartmentAllocation;

/**
 * SOAP Web Service Client consumed by Java Servlets to achieve automated department allocation.
 * Fulfills CO5 requirements.
 */
public class DepartmentAllocationClient {

    private final DepartmentAllocationService service;

    public DepartmentAllocationClient() {
        this.service = new DepartmentAllocationServiceImpl();
    }

    public DepartmentAllocationClient(DepartmentAllocationService service) {
        this.service = service;
    }

    /**
     * Invokes the SOAP service to allocate department and resolution SLA.
     */
    public DepartmentAllocation allocate(String category, String location) {
        DepartmentAllocationResponse response = service.allocateDepartment(category, location);
        return new DepartmentAllocation(
            response.getCategory(),
            response.getLocation(),
            response.getDepartment(),
            response.getSlaHours(),
            response.getPriority()
        );
    }
}
