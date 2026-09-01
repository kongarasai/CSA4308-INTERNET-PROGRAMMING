# Individual Group Member Contribution Matrix: CivicConnect Platform

**Course Code:** CSA4308 – Internet Programming  
**Assignment Title:** CivicConnect: A Multi-Tier Full-Stack Web Platform for Municipal Civic-Issue Reporting and Resolution  

---

## 1. Group Member Ownership Matrix

| Member Name | Assigned Modules & Layer | Specific Implementation Responsibilities | Mapped COs |
| :--- | :--- | :--- | :--- |
| **Member 1 (Lead Frontend)** | HTML5/XHTML, CSS3, JS, DOM, AJAX | • Designed semantic HTML5 pages (`index.html`, `complaint.html`, `tracker.html`)<br>• Authored `style.css` (Grid/Flexbox, Dark Mode, Animations)<br>• Developed `main.js` (Regex validation, Fetch API AJAX, DOM updates) | CO1, CO2 |
| **Member 2 (Backend & MVC)** | Java Servlets, Sessions, JSP, JSTL | • Implemented Java Servlets (`LoginServlet`, `ComplaintServlet`, `ComplaintListServlet`)<br>• Designed `HttpSession` & secure `HttpOnly` cookie handling<br>• Developed `admin-dashboard.jsp` & MVC model separation | CO3, CO4 |
| **Member 3 (XML/SOAP & Quality)** | XML, XSD, XSLT, SOAP, WSDL, Testing, CI | • Created `complaint.xsd`, `complaint-report.xslt`, `sitemap.xml`<br>• Designed SOAP `DepartmentAllocationService` & WSDL contract<br>• Configured JUnit test suite & GitHub Actions workflow | CO4, CO5 |

---

## 2. Detailed Task Verification

### Member 1: Frontend & DOM Scripting
* Implemented client-side regular expressions for Indian mobile numbers (`^[6-9]\d{9}$`) and PIN codes (`^[1-9][0-9]{5}$`).
* Built hand-written responsive design using CSS Grid and Flexbox with media queries for desktop, tablet, and mobile displays.
* Implemented dynamic DOM node creation (`document.createElement`) to append new complaint submissions to the tracker view without reloading the page.

### Member 2: Java Servlet Backend & MVC JSP
* Developed servlet handlers for authentication, registration, complaint processing, and status inquiries.
* Enforced user data isolation so logged-in citizens (`C101`) view only their own submitted grievances.
* Built JSP views using JSTL core tags (`<c:forEach>`, `<c:if>`, `<c:out>`) adhering strictly to the MVC paradigm.

### Member 3: XML Data Interchange, SOAP Web Service & Testing
* Authored `complaint.xsd` defining strict XML structure, pattern restrictions, and category enumerations.
* Implemented XSLT stylesheet (`complaint-report.xslt`) for departmental grouping of complaints.
* Developed `DepartmentAllocationService` JAX-WS SOAP service described by `DepartmentAllocationService.wsdl`.
* Created unit tests (`ComplaintValidationTest`, `XSDValidationTest`, `SOAPServiceTest`, `SecurityTest`) and GitHub Actions workflow (`.github/workflows/build.yml`).
