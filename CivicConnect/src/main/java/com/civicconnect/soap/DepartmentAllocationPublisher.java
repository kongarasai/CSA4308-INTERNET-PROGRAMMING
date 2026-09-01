package com.civicconnect.soap;

import javax.xml.ws.Endpoint;

/**
 * Standalone Publisher for DepartmentAllocationService SOAP Endpoint.
 * Allows running and validating the SOAP service independently or in test suites.
 */
public class DepartmentAllocationPublisher {
    public static final String ENDPOINT_URL = "http://localhost:8088/ws/departmentAllocation";

    public static Endpoint publish() {
        System.out.println("Publishing DepartmentAllocationService SOAP Web Service at: " + ENDPOINT_URL);
        return Endpoint.publish(ENDPOINT_URL, new DepartmentAllocationServiceImpl());
    }

    public static void main(String[] args) {
        Endpoint endpoint = publish();
        System.out.println("SOAP Web Service published successfully. WSDL available at: " + ENDPOINT_URL + "?wsdl");
    }
}
