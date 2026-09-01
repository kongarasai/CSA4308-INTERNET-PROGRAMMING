package com.civicconnect.servlet;

import com.civicconnect.model.Citizen;
import com.civicconnect.util.ComplaintDataStore;
import com.civicconnect.util.SecurityUtil;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

/**
 * Servlet handling Citizen authentication, HttpSession creation, and secure cookie handling.
 * Fulfills CO3 requirement.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/api/login", "/login"})
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String email = SecurityUtil.sanitizeInput(request.getParameter("email"));
        String password = request.getParameter("password");

        JSONObject jsonResponse = new JSONObject();

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Email and password are mandatory.");
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            out.flush();
            return;
        }

        Citizen authenticatedCitizen = ComplaintDataStore.authenticate(email, password);

        if (authenticatedCitizen != null) {
            // Create HttpSession
            HttpSession session = request.getSession(true);
            session.setMaxInactiveInterval(30 * 60); // 30 minutes session timeout
            session.setAttribute("user", authenticatedCitizen);
            session.setAttribute("citizenId", authenticatedCitizen.getCitizenId());
            session.setAttribute("citizenName", authenticatedCitizen.getName());

            // Set secure session identification cookie
            Cookie userCookie = new Cookie("CC_USER", authenticatedCitizen.getCitizenId());
            userCookie.setPath("/");
            userCookie.setMaxAge(30 * 60);
            userCookie.setHttpOnly(true); // Mitigate XSS cookie theft
            response.addCookie(userCookie);

            response.setStatus(HttpServletResponse.SC_OK);
            jsonResponse.put("success", true);
            jsonResponse.put("message", "Authentication successful.");
            jsonResponse.put("citizenId", authenticatedCitizen.getCitizenId());
            jsonResponse.put("citizenName", authenticatedCitizen.getName());

            if (authenticatedCitizen.getCitizenId().startsWith("ADMIN")) {
                jsonResponse.put("redirectUrl", "admin-dashboard.jsp");
            } else {
                jsonResponse.put("redirectUrl", "tracker.html");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Invalid email address or password.");
        }

        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();
    }
}
