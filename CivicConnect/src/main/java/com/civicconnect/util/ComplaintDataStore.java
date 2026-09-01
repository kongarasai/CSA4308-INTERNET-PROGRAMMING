package com.civicconnect.util;

import com.civicconnect.model.Citizen;
import com.civicconnect.model.Complaint;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Thread-safe Data Store for CivicConnect complaints and citizen accounts.
 * Provides sample pre-populated complaints across required municipal categories.
 */
public class ComplaintDataStore {

    private static final List<Complaint> complaintsList = Collections.synchronizedList(new ArrayList<>());
    private static final Map<String, Citizen> citizenMap = Collections.synchronizedMap(new HashMap<>());
    private static int complaintCounter = 1001;

    static {
        // Populate default demo citizen accounts for authentication
        Citizen defaultCitizen = new Citizen("C101", "Rajesh Kumar", "9876543210", "rajesh@example.com", "password123", "560001");
        Citizen adminCitizen = new Citizen("ADMIN1", "Municipal Admin", "9123456789", "admin@civicconnect.gov.in", "admin123", "560001");
        
        citizenMap.put(defaultCitizen.getEmail().toLowerCase(), defaultCitizen);
        citizenMap.put(adminCitizen.getEmail().toLowerCase(), adminCitizen);
    }

    public static synchronized String generateNextId() {
        complaintCounter++;
        return "CMP" + complaintCounter;
    }

    public static void addComplaint(Complaint complaint) {
        if (complaint != null) {
            complaintsList.add(0, complaint); // Add newest first
        }
    }

    public static List<Complaint> getAllComplaints() {
        return new ArrayList<>(complaintsList);
    }

    public static List<Complaint> getComplaintsByCitizen(String citizenId) {
        List<Complaint> result = new ArrayList<>();
        if (citizenId == null) return result;
        synchronized (complaintsList) {
            for (Complaint c : complaintsList) {
                if (citizenId.equalsIgnoreCase(c.getCitizenId())) {
                    result.add(c);
                }
            }
        }
        return result;
    }

    public static Citizen registerCitizen(Citizen citizen) {
        if (citizen != null && citizen.getEmail() != null) {
            citizenMap.put(citizen.getEmail().toLowerCase(), citizen);
            return citizen;
        }
        return null;
    }

    public static Citizen authenticate(String email, String password) {
        if (email == null || password == null) return null;
        Citizen citizen = citizenMap.get(email.toLowerCase().trim());
        if (citizen != null && citizen.getPassword().equals(password)) {
            return citizen;
        }
        return null;
    }

    public static String toAggregatedXML() {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<complaints xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n");
        synchronized (complaintsList) {
            for (Complaint c : complaintsList) {
                xml.append("  <complaint>\n");
                xml.append("    <complaintId>").append(c.getComplaintId()).append("</complaintId>\n");
                xml.append("    <citizenId>").append(c.getCitizenId()).append("</citizenId>\n");
                xml.append("    <citizenName>").append(c.getCitizenName()).append("</citizenName>\n");
                xml.append("    <category>").append(c.getCategory()).append("</category>\n");
                xml.append("    <description>").append(SecurityUtil.sanitizeInput(c.getDescription())).append("</description>\n");
                xml.append("    <location>").append(SecurityUtil.sanitizeInput(c.getLocation())).append("</location>\n");
                xml.append("    <pincode>").append(c.getPincode()).append("</pincode>\n");
                xml.append("    <timestamp>").append(c.getTimestamp()).append("</timestamp>\n");
                xml.append("    <status>").append(c.getStatus()).append("</status>\n");
                xml.append("    <department>").append(c.getDepartment()).append("</department>\n");
                xml.append("    <sla>").append(c.getSla()).append("</sla>\n");
                xml.append("    <priority>").append(c.getPriority()).append("</priority>\n");
                xml.append("  </complaint>\n");
            }
        }
        xml.append("</complaints>");
        return xml.toString();
    }
}
