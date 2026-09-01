package com.civicconnect.soap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Genuine SOAP Web Service Service Endpoint Interface (SEI).
 * Course Outcome 5: JAX-WS / WSDL / SOAP Web Service integration.
 */
@WebService(
    name = "DepartmentAllocationPortType",
    targetNamespace = "http://soap.civicconnect.com/"
)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public interface DepartmentAllocationService {

    @WebMethod(operationName = "allocateDepartment")
    @WebResult(name = "departmentAllocationResult")
    DepartmentAllocationResponse allocateDepartment(
        @WebParam(name = "category") String category,
        @WebParam(name = "location") String location
    );
}
