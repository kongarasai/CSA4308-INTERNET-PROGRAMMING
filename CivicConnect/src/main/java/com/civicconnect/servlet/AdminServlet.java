package com.civicconnect.servlet;

import com.civicconnect.model.Complaint;
import com.civicconnect.util.ComplaintDataStore;
import com.civicconnect.util.XSLTTransformer;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller Servlet in the MVC Architecture.
 * Intercepts admin requests, populates JavaBeans Model, triggers XSLT report transformation,
 * and forwards to JSP View (admin-dashboard.jsp).
 * Fulfills CO4 (JSP / MVC / XSLT).
 */
@WebServlet(name = "AdminServlet", urlPatterns = {"/admin", "/admin/dashboard", "/admin/report"})
public class AdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // Retrieve Model data
        List<Complaint> complaintsList = ComplaintDataStore.getAllComplaints();
        request.setAttribute("complaints", complaintsList);
        request.setAttribute("totalCount", complaintsList.size());

        // Perform XSLT Transformation of aggregated XML grouped by department
        String rawXML = ComplaintDataStore.toAggregatedXML();
        InputStream xsltStream = getServletContext().getResourceAsStream("/WEB-INF/xml/complaint-report.xslt");

        String transformedHTMLReport = "";
        if (xsltStream != null) {
            try {
                transformedHTMLReport = XSLTTransformer.transformXMLToHTML(rawXML, xsltStream);
            } catch (Exception e) {
                transformedHTMLReport = "<div class='alert error'>XSLT Transformation Error: " + e.getMessage() + "</div>";
            }
        } else {
            transformedHTMLReport = "<div class='alert info'>XSLT stylesheet ready at /WEB-INF/xml/complaint-report.xslt.</div>";
        }

        request.setAttribute("xsltReport", transformedHTMLReport);

        // Forward to appropriate JSP View (MVC pattern - Zero scriptlets)
        String path = request.getServletPath();
        if ("/admin/report".equals(path)) {
            request.getRequestDispatcher("/complaint-report.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/admin-dashboard.jsp").forward(request, response);
        }
    }
}
