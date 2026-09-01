<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>XSLT Department Grouped Report - CivicConnect</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <header class="header-nav">
        <div class="nav-container">
            <a href="index.html" class="brand-logo">🏛️ CivicConnect</a>
            <nav>
                <ul class="nav-menu">
                    <li><a href="index.html" class="nav-link">Home</a></li>
                    <li><a href="admin" class="nav-link">Admin Portal</a></li>
                    <li><a href="admin/report" class="nav-link active">XSLT Report</a></li>
                    <li><button type="button" class="dark-toggle-btn" id="dark-mode-toggle">🌙 Dark Mode</button></li>
                </ul>
            </nav>
        </div>
    </header>

    <main class="container">
        <div class="card">
            <h1 style="font-size: 1.6rem; font-weight: 800; margin-bottom: 0.5rem;">
                📊 XSLT XML Departmental Transformation Report
            </h1>
            <p style="color: var(--text-secondary); margin-bottom: 1.5rem; font-size: 0.9rem;">
                Server-side transformation of structured complaint XML data into an HTML report grouped by municipal department using <code>complaint-report.xslt</code>. Rendered strictly via Controller (AdminServlet) &amp; JSP View (JSTL/EL) with zero scriptlets. Fulfills CO4 requirements.
            </p>

            <div class="xslt-output">
                <c:out value="${xsltReport}" escapeXml="false"/>
            </div>
        </div>
    </main>

    <footer class="site-footer">
        <div class="container" style="margin: 0 auto;">
            <p>&copy; 2026 CivicConnect Platform | XSLT Transformation View (CO4 &amp; MVC)</p>
        </div>
    </footer>
    <script src="js/main.js"></script>
</body>
</html>
