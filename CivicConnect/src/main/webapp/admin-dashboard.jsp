<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard & XSLT Report - CivicConnect</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

    <!-- Header Navigation -->
    <header class="header-nav">
        <div class="nav-container">
            <a href="index.html" class="brand-logo">🏛️ CivicConnect <span class="badge-city">ADMIN PORTAL</span></a>
            <nav>
                <ul class="nav-menu">
                    <li><a href="index.html" class="nav-link">Home</a></li>
                    <li><a href="admin" class="nav-link active">Dashboard</a></li>
                    <li><a href="complaint-report.jsp" class="nav-link">XSLT Department Report</a></li>
                    <li><a href="api/logout" class="btn btn-outline" style="color: #ef4444;">Logout</a></li>
                    <li><button type="button" class="dark-toggle-btn" id="dark-mode-toggle">🌙 Dark Mode</button></li>
                </ul>
            </nav>
        </div>
    </header>

    <main class="container">
        
        <!-- Header Banner -->
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
            <div>
                <h1 style="font-size: 1.8rem; font-weight: 800;">Municipal Corporation Administration Dashboard</h1>
                <p style="color: var(--text-secondary); font-size: 0.9rem;">
                    JSP View implementing Model-View-Controller (MVC) paradigm with JSTL core tag rendering.
                </p>
            </div>
            <div style="text-align: right;">
                <span class="badge" style="background-color: var(--primary-color); color: #fff; padding: 0.5rem 1rem; font-size: 0.9rem;">
                    Total Complaints: <c:out value="${totalCount}" default="0"/>
                </span>
            </div>
        </div>

        <!-- System Architecture & Status Summary Cards -->
        <div class="grid-3" style="margin-bottom: 2rem;">
            <div class="card">
                <h3 class="card-title">🧩 Architecture Tier</h3>
                <p style="color: var(--text-secondary); font-size: 0.85rem;">
                    <strong>Controller:</strong> AdminServlet / ComplaintServlet<br>
                    <strong>Model:</strong> Complaint &amp; Citizen JavaBeans<br>
                    <strong>View:</strong> JSP with JSTL tags
                </p>
            </div>
            <div class="card">
                <h3 class="card-title">📄 XML Schema Integrity</h3>
                <p style="color: var(--text-secondary); font-size: 0.85rem;">
                    <strong>Validation Schema:</strong> <code>complaint.xsd</code><br>
                    <strong>SOAP Contract:</strong> <code>DepartmentAllocationService.wsdl</code><br>
                    <strong>Status:</strong> Active &amp; Validated
                </p>
            </div>
            <div class="card">
                <h3 class="card-title">⚡ SOAP Web Service Routing</h3>
                <p style="color: var(--text-secondary); font-size: 0.85rem;">
                    <strong>Endpoint:</strong> <code>DepartmentAllocationPort</code><br>
                    <strong>Automated SLAs:</strong> 12h - 36h rule matrix<br>
                    <strong>Protocol:</strong> SOAP 1.1 / JAX-WS
                </p>
            </div>
        </div>

        <!-- Tabbed Navigation / Toggle -->
        <div class="card" style="margin-bottom: 2rem;">
            <h2 style="font-size: 1.35rem; font-weight: 700; margin-bottom: 1rem;">
                📋 Live Complaint Registry (Rendered via JSTL &amp; MVC Model)
            </h2>

            <div class="table-responsive">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Complaint ID</th>
                            <th>Citizen</th>
                            <th>Category</th>
                            <th>Description</th>
                            <th>Location</th>
                            <th>Allocated Department (SOAP)</th>
                            <th>SLA</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty complaints}">
                                <c:forEach var="c" items="${complaints}">
                                    <tr>
                                        <td><strong><c:out value="${c.complaintId}"/></strong></td>
                                        <td><c:out value="${c.citizenName}"/> (<c:out value="${c.citizenId}"/>)</td>
                                        <td><span class="tag"><c:out value="${c.category}"/></span></td>
                                        <td style="max-width: 240px;"><c:out value="${c.description}"/></td>
                                        <td><c:out value="${c.location}"/></td>
                                        <td><strong><c:out value="${c.department}"/></strong></td>
                                        <td><span class="badge sla-badge"><c:out value="${c.sla}"/></span></td>
                                        <td>
                                            <span class="badge status-badge ${c.status.toLowerCase()}">
                                                <c:out value="${c.status}"/>
                                            </span>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="8" style="text-align: center; color: var(--text-secondary);">
                                        No complaints currently logged in the system datastore.
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Embedded XSLT Transformed Department Report Section -->
        <div class="card">
            <h2 style="font-size: 1.35rem; font-weight: 700; margin-bottom: 1rem;">
                📊 XSLT Transformed Departmental Summary Report
            </h2>
            <p style="color: var(--text-secondary); margin-bottom: 1.5rem; font-size: 0.85rem;">
                The report below is dynamically transformed from raw <code>complaints.xml</code> using <code>complaint-report.xslt</code> on the server side via <code>XSLTTransformer.java</code>.
            </p>

            <div class="xslt-output-wrapper">
                ${xsltReport}
            </div>
        </div>

    </main>

    <footer class="site-footer">
        <div class="container" style="margin: 0 auto;">
            <p>&copy; 2026 CivicConnect Platform | MVC Paradigm (Servlet + JavaBean + JSP/JSTL)</p>
        </div>
    </footer>

    <script src="js/main.js"></script>
</body>
</html>
