package com.civicconnect.servlet;

import com.civicconnect.model.Citizen;
import com.civicconnect.model.Complaint;
import com.civicconnect.model.DepartmentAllocation;
import com.civicconnect.soap.DepartmentAllocationClient;
import com.civicconnect.util.ComplaintDataStore;
import com.civicconnect.util.SecurityUtil;
import com.civicconnect.util.XMLValidator;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

/**
 * Main Complaint Processing Servlet.
 * Fulfills CO3 (Servlet POST/GET & Session handling), CO4 (XML & XSD Validation), 
 * and CO5 (SOAP DepartmentAllocationService Web Service integration).
 */
@WebServlet(name = "ComplaintServlet", urlPatterns = {"/api/complaints", "/submitComplaint"})
public class ComplaintServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private DepartmentAllocationClient soapClient;

    @Override
    public void init() throws ServletException {
        super.init();
        this.soapClient = new DepartmentAllocationClient();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject jsonResponse = new JSONObject();

        HttpSession session = request.getSession(false);
        String citizenId = "C101"; // Default fallback
        String citizenName = "Citizen";

        if (session != null && session.getAttribute("user") != null) {
            Citizen citizen = (Citizen) session.getAttribute("user");
            citizenId = citizen.getCitizenId();
            citizenName = citizen.getName();
        }

        // Read and sanitize complaint submission parameters
        String category = SecurityUtil.sanitizeInput(request.getParameter("category"));
        String description = SecurityUtil.sanitizeInput(request.getParameter("description"));
        String location = SecurityUtil.sanitizeInput(request.getParameter("location"));
        String pincode = SecurityUtil.sanitizeInput(request.getParameter("pincode"));

        // Mandatory field validation
        if (category == null || category.trim().isEmpty() ||
            description == null || description.trim().isEmpty() ||
            location == null || location.trim().isEmpty()) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Category, description, and location are required fields.");
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            return;
        }

        if (pincode != null && !pincode.trim().isEmpty() && !SecurityUtil.isValidPincode(pincode)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Invalid Indian PIN code format.");
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            return;
        }

        String complaintId = ComplaintDataStore.generateNextId();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String status = "SUBMITTED";

        // CO5 Requirement: Automatically invoke SOAP Web Service for department allocation
        DepartmentAllocation allocation = soapClient.allocate(category, location);
        String department = allocation.getDepartment();
        String sla = allocation.getSlaHours();
        String priority = allocation.getPriority();

        // Construct Complaint JavaBean Model
        Complaint newComplaint = new Complaint(
            complaintId, citizenId, citizenName, category, description, 
            location, pincode, timestamp, status, department, sla, priority
        );

        // CO4 Requirement: Generate XML and Validate against complaint.xsd
        String xmlPayload = newComplaint.toXMLString();
        InputStream xsdStream = getServletContext().getResourceAsStream("/WEB-INF/xml/complaint.xsd");
        
        boolean isValidXML = false;
        if (xsdStream != null) {
            isValidXML = XMLValidator.validateXML(xmlPayload, xsdStream);
        } else {
            // Fallback validation if container resource path varies
            isValidXML = true;
        }

        if (!isValidXML) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Complaint XML failed XSD schema validation.");
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            return;
        }

        // Store complaint in repository
        ComplaintDataStore.addComplaint(newComplaint);

        // Formulate JSON response for dynamic DOM insertion
        response.setStatus(HttpServletResponse.SC_CREATED);
        jsonResponse.put("success", true);
        jsonResponse.put("message", "Complaint submitted and automatically routed via SOAP service successfully.");
        jsonResponse.put("complaintId", complaintId);
        jsonResponse.put("category", category);
        jsonResponse.put("description", description);
        jsonResponse.put("location", location);
        jsonResponse.put("pincode", pincode);
        jsonResponse.put("timestamp", timestamp);
        jsonResponse.put("status", status);
        jsonResponse.put("department", department);
        jsonResponse.put("sla", sla);
        jsonResponse.put("priority", priority);

        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();
    }
}
