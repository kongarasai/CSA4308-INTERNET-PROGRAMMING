package com.civicconnect.model;

import java.io.Serializable;

/**
 * JavaBean representing a Civic Complaint in CivicConnect.
 * Formats data according to the complaint.xsd schema definition.
 */
public class Complaint implements Serializable {
    private static final long serialVersionUID = 1L;

    private String complaintId;
    private String citizenId;
    private String citizenName;
    private String category;
    private String description;
    private String location;
    private String pincode;
    private String timestamp;
    private String status;
    private String department;
    private String sla;
    private String priority;

    public Complaint() {}

    public Complaint(String complaintId, String citizenId, String citizenName, String category, 
                     String description, String location, String pincode, String timestamp, 
                     String status, String department, String sla, String priority) {
        this.complaintId = complaintId;
        this.citizenId = citizenId;
        this.citizenName = citizenName;
        this.category = category;
        this.description = description;
        this.location = location;
        this.pincode = pincode;
        this.timestamp = timestamp;
        this.status = status;
        this.department = department;
        this.sla = sla;
        this.priority = priority;
    }

    public String getComplaintId() { return complaintId; }
    public void setComplaintId(String complaintId) { this.complaintId = complaintId; }

    public String getCitizenId() { return citizenId; }
    public void setCitizenId(String citizenId) { this.citizenId = citizenId; }

    public String getCitizenName() { return citizenName; }
    public void setCitizenName(String citizenName) { this.citizenName = citizenName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getSla() { return sla; }
    public void setSla(String sla) { this.sla = sla; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    /**
     * Serializes this complaint to XML string adhering to complaint.xsd.
     */
    public String toXMLString() {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<complaint xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n");
        xml.append("    <complaintId>").append(escapeXML(complaintId)).append("</complaintId>\n");
        xml.append("    <citizenId>").append(escapeXML(citizenId != null ? citizenId : "C101")).append("</citizenId>\n");
        xml.append("    <citizenName>").append(escapeXML(citizenName != null ? citizenName : "Citizen")).append("</citizenName>\n");
        xml.append("    <category>").append(escapeXML(category)).append("</category>\n");
        xml.append("    <description>").append(escapeXML(description)).append("</description>\n");
        xml.append("    <location>").append(escapeXML(location)).append("</location>\n");
        xml.append("    <pincode>").append(escapeXML(pincode != null ? pincode : "560001")).append("</pincode>\n");
        xml.append("    <timestamp>").append(escapeXML(timestamp)).append("</timestamp>\n");
        xml.append("    <status>").append(escapeXML(status)).append("</status>\n");
        xml.append("    <department>").append(escapeXML(department != null ? department : "UNASSIGNED")).append("</department>\n");
        xml.append("    <sla>").append(escapeXML(sla != null ? sla : "48 Hours")).append("</sla>\n");
        xml.append("    <priority>").append(escapeXML(priority != null ? priority : "MEDIUM")).append("</priority>\n");
        xml.append("</complaint>");
        return xml.toString();
    }

    private String escapeXML(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;");
    }
}
