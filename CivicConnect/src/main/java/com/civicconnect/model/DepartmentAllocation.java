package com.civicconnect.model;

import java.io.Serializable;

/**
 * JavaBean representing SOAP Web Service response for department allocation and SLA resolution.
 */
public class DepartmentAllocation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String category;
    private String location;
    private String department;
    private String slaHours;
    private String priority;

    public DepartmentAllocation() {}

    public DepartmentAllocation(String category, String location, String department, String slaHours, String priority) {
        this.category = category;
        this.location = location;
        this.department = department;
        this.slaHours = slaHours;
        this.priority = priority;
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
}
