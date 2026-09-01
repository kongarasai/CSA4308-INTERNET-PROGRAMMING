package com.civicconnect.soap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "DepartmentAllocationResponse", namespace = "http://soap.civicconnect.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DepartmentAllocationResponse", namespace = "http://soap.civicconnect.com/", propOrder = {
    "category",
    "location",
    "department",
    "slaHours",
    "priority",
    "status"
})
public class DepartmentAllocationResponse {

    @XmlElement(required = true)
    private String category;

    @XmlElement(required = true)
    private String location;

    @XmlElement(required = true)
    private String department;

    @XmlElement(required = true)
    private String slaHours;

    @XmlElement(required = true)
    private String priority;

    @XmlElement(required = true)
    private String status;

    public DepartmentAllocationResponse() {}

    public DepartmentAllocationResponse(String category, String location, String department, String slaHours, String priority, String status) {
        this.category = category;
        this.location = location;
        this.department = department;
        this.slaHours = slaHours;
        this.priority = priority;
        this.status = status;
    }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getSlaHours() { return slaHours; }
    public void setSlaHours(String slaHours) { this.slaHours = slaHours; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
