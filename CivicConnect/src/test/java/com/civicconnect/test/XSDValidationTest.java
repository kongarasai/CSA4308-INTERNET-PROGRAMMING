package com.civicconnect.test;

import com.civicconnect.model.Complaint;
import com.civicconnect.util.XMLValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CO4 XML Schema (XSD) Validation Test Suite")
public class XSDValidationTest {

    private final String xsdSchemaContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" elementFormDefault=\"qualified\">\n" +
            "    <xs:element name=\"complaint\" type=\"ComplaintType\"/>\n" +
            "    <xs:complexType name=\"ComplaintType\">\n" +
            "        <xs:sequence>\n" +
            "            <xs:element name=\"complaintId\" type=\"xs:string\"/>\n" +
            "            <xs:element name=\"citizenId\" type=\"xs:string\"/>\n" +
            "            <xs:element name=\"citizenName\" type=\"xs:string\"/>\n" +
            "            <xs:element name=\"category\" type=\"xs:string\"/>\n" +
            "            <xs:element name=\"description\" type=\"xs:string\"/>\n" +
            "            <xs:element name=\"location\" type=\"xs:string\"/>\n" +
            "            <xs:element name=\"pincode\" type=\"xs:string\"/>\n" +
            "            <xs:element name=\"timestamp\" type=\"xs:string\"/>\n" +
            "            <xs:element name=\"status\" type=\"xs:string\"/>\n" +
            "            <xs:element name=\"department\" type=\"xs:string\"/>\n" +
            "            <xs:element name=\"sla\" type=\"xs:string\"/>\n" +
            "            <xs:element name=\"priority\" type=\"xs:string\"/>\n" +
            "        </xs:sequence>\n" +
            "    </xs:complexType>\n" +
            "</xs:schema>";

    @Test
    @DisplayName("Validate Valid Complaint XML Document Against XSD")
    public void testValidComplaintXML() {
        Complaint complaint = new Complaint(
            "CMP1001", "C101", "Rajesh Kumar", "POTHOLE",
            "Pothole on MG Road near bus stop.", "MG Road", "560001",
            "2026-09-01T10:00:00", "SUBMITTED", "Roads & Infrastructure", "24 Hours", "HIGH"
        );

        String xml = complaint.toXMLString();
        InputStream xsdStream = new ByteArrayInputStream(xsdSchemaContent.getBytes(StandardCharsets.UTF_8));

        boolean isValid = XMLValidator.validateXML(xml, xsdStream);
        assertTrue(isValid, "Valid complaint XML must pass XSD validation");
    }

    @Test
    @DisplayName("Reject Malformed XML Document")
    public void testMalformedXML() {
        String malformedXML = "<?xml version=\"1.0\"?><complaint><complaintId>CMP1001</complaint>"; // Missing closing tag
        InputStream xsdStream = new ByteArrayInputStream(xsdSchemaContent.getBytes(StandardCharsets.UTF_8));

        boolean isValid = XMLValidator.validateXML(malformedXML, xsdStream);
        assertFalse(isValid, "Malformed XML must be rejected");
    }
}
