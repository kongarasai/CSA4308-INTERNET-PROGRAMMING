package com.civicconnect.util;

import java.io.InputStream;
import java.io.StringReader;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

/**
 * Utility for validating Complaint XML documents against complaint.xsd.
 * Fulfills CO4 requirements for XML/XSD validation.
 */
public class XMLValidator {

    /**
     * Validates XML content string against specified XSD schema input stream.
     */
    public static boolean validateXML(String xmlContent, InputStream xsdInputStream) {
        if (xmlContent == null || xsdInputStream == null) {
            return false;
        }
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            // Disable DTDs and External Entities for Security (XXE Prevention)
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            
            Schema schema = factory.newSchema(new StreamSource(xsdInputStream));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xmlContent)));
            return true;
        } catch (SAXException e) {
            System.err.println("[XMLValidator] SAX Validation Error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("[XMLValidator] Exception: " + e.getMessage());
            return false;
        }
    }
}
