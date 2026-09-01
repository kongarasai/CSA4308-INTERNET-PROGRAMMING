package com.civicconnect.util;

import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Utility for transforming raw Complaint XML documents into formatted HTML reports using XSLT.
 * Fulfills CO4 requirements for XSLT transformation.
 */
public class XSLTTransformer {

    /**
     * Transforms XML string using XSLT input stream into an HTML string.
     */
    public static String transformXMLToHTML(String xmlContent, InputStream xsltInputStream) throws Exception {
        if (xmlContent == null || xsltInputStream == null) {
            throw new IllegalArgumentException("XML content or XSLT stream cannot be null");
        }

        TransformerFactory factory = TransformerFactory.newInstance();
        // Security against XXE / malicious external entities during transformation
        factory.setFeature(javax.xml.XMLConstants.FEATURE_SECURE_PROCESSING, true);

        Transformer transformer = factory.newTransformer(new StreamSource(xsltInputStream));
        StringWriter writer = new StringWriter();
        
        transformer.transform(new StreamSource(new StringReader(xmlContent)), new StreamResult(writer));
        return writer.toString();
    }
}
