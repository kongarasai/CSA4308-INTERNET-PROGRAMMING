package com.civicconnect.test;

import com.civicconnect.model.Citizen;
import com.civicconnect.model.Complaint;
import com.civicconnect.util.ComplaintDataStore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CO3 Servlet & DataStore Unit Test Suite")
public class ServletUnitTest {

    @Test
    @DisplayName("Test Citizen Authentication and Credential Matching")
    public void testAuthentication() {
        Citizen authenticated = ComplaintDataStore.authenticate("rajesh@example.com", "password123");
        assertNotNull(authenticated);
        assertEquals("C101", authenticated.getCitizenId());
        assertEquals("Rajesh Kumar", authenticated.getName());

        Citizen invalid = ComplaintDataStore.authenticate("rajesh@example.com", "wrongpassword");
        assertNull(invalid);
    }

    @Test
    @DisplayName("Test User-Isolated Complaint Retrieval")
    public void testUserIsolatedComplaints() {
        List<Complaint> userComplaints = ComplaintDataStore.getComplaintsByCitizen("C101");
        assertNotNull(userComplaints);
        assertFalse(userComplaints.isEmpty());
        for (Complaint c : userComplaints) {
            assertEquals("C101", c.getCitizenId(), "Citizen must only see complaints matching their Citizen ID");
        }
    }

    @Test
    @DisplayName("Test New Complaint Submission and ID Generation")
    public void testComplaintSubmission() {
        String newId = ComplaintDataStore.generateNextId();
        assertNotNull(newId);
        assertTrue(newId.startsWith("CMP"));

        Complaint complaint = new Complaint(
            newId, "C101", "Rajesh Kumar", "POTHOLE",
            "Road crater near school zone.", "School Road", "560001",
            "2026-09-01T12:00:00", "SUBMITTED", "Roads & Infrastructure", "24 Hours", "HIGH"
        );

        ComplaintDataStore.addComplaint(complaint);
        List<Complaint> updatedList = ComplaintDataStore.getAllComplaints();
        assertTrue(updatedList.stream().anyMatch(c -> c.getComplaintId().equals(newId)));
    }
}
