package com.civicconnect.servlet;

import com.civicconnect.model.Citizen;
import com.civicconnect.util.ComplaintDataStore;
import com.civicconnect.util.SecurityUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 * Servlet handling new Citizen user registration.
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/api/register", "/register"})
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject jsonResponse = new JSONObject();

        String name = SecurityUtil.sanitizeInput(request.getParameter("name"));
        String mobile = SecurityUtil.sanitizeInput(request.getParameter("mobile"));
        String email = SecurityUtil.sanitizeInput(request.getParameter("email"));
        String password = request.getParameter("password");
        String pincode = SecurityUtil.sanitizeInput(request.getParameter("pincode"));

        // Server-side Regex validation
        if (name == null || name.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "All mandatory fields must be filled.");
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            return;
        }

        if (!SecurityUtil.isValidMobile(mobile)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Invalid Indian mobile number. Must be 10 digits starting with 6-9.");
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            return;
        }

        if (!SecurityUtil.isValidPincode(pincode)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Invalid PIN code. Must be a valid 6-digit Indian PIN code.");
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            return;
        }

        String citizenId = "C" + (100 + (int)(Math.random() * 899));
        Citizen newCitizen = new Citizen(citizenId, name, mobile, email, password, pincode);

        ComplaintDataStore.registerCitizen(newCitizen);

        response.setStatus(HttpServletResponse.SC_CREATED);
        jsonResponse.put("success", true);
        jsonResponse.put("message", "Registration successful. You can now login.");
        jsonResponse.put("citizenId", citizenId);

        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();
    }
}
