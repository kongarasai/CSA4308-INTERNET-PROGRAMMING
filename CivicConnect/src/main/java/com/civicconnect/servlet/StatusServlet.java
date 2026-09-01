package com.civicconnect.servlet;

import com.civicconnect.model.Complaint;
import com.civicconnect.util.ComplaintDataStore;
import com.civicconnect.util.SecurityUtil;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 * Servlet for checking real-time complaint status by Complaint ID.
 */
@WebServlet(name = "StatusServlet", urlPatterns = {"/api/status", "/checkStatus"})
public class StatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject jsonResponse = new JSONObject();

        String id = SecurityUtil.sanitizeInput(request.getParameter("id"));

        if (id == null || id.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Complaint ID is required.");
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            return;
        }

        Complaint found = null;
        for (Complaint c : ComplaintDataStore.getAllComplaints()) {
            if (id.equalsIgnoreCase(c.getComplaintId())) {
                found = c;
                break;
            }
        }

        if (found != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            jsonResponse.put("success", true);
            jsonResponse.put("complaintId", found.getComplaintId());
            jsonResponse.put("category", found.getCategory());
            jsonResponse.put("description", found.getDescription());
            jsonResponse.put("location", found.getLocation());
            jsonResponse.put("status", found.getStatus());
            jsonResponse.put("department", found.getDepartment());
            jsonResponse.put("sla", found.getSla());
            jsonResponse.put("timestamp", found.getTimestamp());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "No complaint found matching ID: " + id);
        }

        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();
    }
}
