# CivicConnect Platform 🏛️
### A Multi-Tier Full-Stack Web Platform for Municipal Civic-Issue Reporting & Resolution

![Build Status](https://github.com/kongarasai/CSA4308-INTERNET-PROGRAMMING/actions/workflows/build.yml/badge.svg)
**Course Code:** CSA4308 – Internet Programming  
**Technology Stack:** HTML5/XHTML, CSS3, Vanilla JavaScript, Java Servlets, JSP/JSTL, JavaBeans, XML, XSD, XSLT, JAX-WS SOAP Web Services, Apache Tomcat  
**Mapped Sustainable Development Goals:** UN SDG 9 (Industry, Innovation and Infrastructure) & UN SDG 11 (Sustainable Cities and Communities)  

---

## 🌟 Executive Overview
**CivicConnect** modernizes municipal grievance redressal by replacing legacy paper workflows with a transparent, tech-driven multi-tier web platform. Citizens report civic issues (potholes, garbage overflow, broken streetlights, water leakage) via a responsive frontend. The platform validates inputs using client/server regular expressions, transmits data asynchronously via AJAX JSON payloads, formats data into XML validated against an XML Schema (`complaint.xsd`), and automatically routes complaints to responsible municipal departments (Roads, Sanitation, Electricity, Water Supply) via a genuine **JAX-WS SOAP Web Service** described by a **WSDL** contract.

---

## 📸 Key Architecture & Course Outcome (CO) Mapping

| Mapped CO | Key Technologies Implemented | Key Artifact Locations |
| :--- | :--- | :--- |
| **CO1: HTML5/XHTML & HTTP** | Semantic HTML5, XML Sitemap, HTTP GET/POST headers | `src/main/webapp/*.html`, `src/main/webapp/sitemap.xml` |
| **CO2: CSS3 & JS/DOM** | CSS Grid/Flexbox, Dark Mode, Regex, Fetch API AJAX, DOM | `src/main/webapp/css/style.css`, `src/main/webapp/js/main.js` |
| **CO3: Java Servlets & Sessions** | Core Java Servlets, `HttpSession`, `HttpOnly` Cookies | `src/main/java/com/civicconnect/servlet/*.java` |
| **CO4: XML, XSD, XSLT & MVC** | XML Schema, XSLT Grouped Report, JavaBeans, JSP/JSTL | `src/main/webapp/WEB-INF/xml/*`, `src/main/webapp/admin-dashboard.jsp` |
| **CO5: SOAP & WSDL** | JAX-WS Web Service, WSDL contract, SOAP Client | `src/main/java/com/civicconnect/soap/*`, `src/main/webapp/WEB-INF/wsdl/*` |

---

## 📁 Repository Directory Structure

```
CivicConnect/
├── .github/
│   └── workflows/
│       └── build.yml                        # GitHub Actions CI Build & Automated Test Workflow
├── docs/
│   ├── TECHNICAL_REPORT.md                  # Comprehensive Technical Academic Report
│   ├── INDIVIDUAL_REFLECTION.md             # One-Page Mandatory Individual Reflection
│   ├── INDIVIDUAL_CONTRIBUTION.md           # Group Member Responsibility Breakdown Matrix
│   └── TEST_PLAN.md                         # Comprehensive Automated/Manual & Load Test Plan
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── civicconnect/
│   │   │           ├── model/               # JavaBeans Models (Complaint, Citizen, DepartmentAllocation)
│   │   │           ├── servlet/             # Java Servlets (Login, Register, Complaint, Admin, etc.)
│   │   │           ├── soap/                # JAX-WS SOAP Service, SEI, SIB, Publisher & Client
│   │   │           └── util/                # XMLValidator, XSLTTransformer, SecurityUtil, DataStore
│   │   └── webapp/
│   │       ├── css/
│   │       │   └── style.css                # Hand-written CSS3 Grid, Flexbox, Animations & Dark Mode
│   │       ├── js/
│   │       │   └── main.js                  # Vanilla JS DOM Scripting, Regex Validations & AJAX
│   │       ├── WEB-INF/
│   │       │   ├── web.xml                  # Web Application Deployment Descriptor
│   │       │   ├── xml/
│   │       │   │   ├── complaint.xml        # Sample XML Instance Document
│   │       │   │   ├── complaints.xml       # Collection XML Instance Document
│   │       │   │   ├── complaint.xsd        # XML Schema Validation Definition
│   │       │   │   └── complaint-report.xslt# XSLT Stylesheet for Department-Grouped HTML Report
│   │       │   └── wsdl/
│   │       │       └── DepartmentAllocationService.wsdl # SOAP Web Service Contract Document
│   │       ├── index.html                   # Citizen Home Page
│   │       ├── complaint.html               # Complaint Submission Form
│   │       ├── tracker.html                 # Real-Time Complaint Status Tracker
│   │       ├── history.html                 # User Complaint History Page
│   │       ├── login.html                   # Citizen / Admin Login Page
│   │       ├── register.html                # Citizen Account Registration Page
│   │       ├── admin-dashboard.jsp          # Admin Dashboard (MVC View with JSTL Tags)
│   │       ├── complaint-report.jsp         # Dedicated XSLT XML Transformation View Page
│   │       ├── sitemap.xml                  # XML Sitemap defining site page hierarchy
│   │       ├── error.html                   # 404/500 Error Page
│   │       └── success.html                 # Action Acknowledgement Page
│   └── test/
│       └── java/
│           └── com/
│               └── civicconnect/
│                   └── test/                # Automated JUnit 5 Test Suite
│                       ├── ComplaintValidationTest.java
│                       ├── XSDValidationTest.java
│                       ├── SOAPServiceTest.java
│                       ├── ServletUnitTest.java
│                       └── SecurityTest.java
└── pom.xml                                  # Maven Project Build Descriptor
```

---

## 🚀 Setup & Deployment Instructions (Apache Tomcat)

### Prerequisites
* **Java Development Kit (JDK):** Version 11 or higher
* **Build Tool:** Apache Maven 3.8+
* **Servlet Container:** Apache Tomcat 9.0+ or Tomcat 10.0+

### Step-by-Step Deployment
1. **Clone Repository:**
   ```bash
   git clone https://github.com/kongarasai/CSA4308-INTERNET-PROGRAMMING.git
   cd final\ assignment
   ```

2. **Build Web Application Package (WAR):**
   ```bash
   mvn clean package
   ```
   This generates `target/CivicConnect.war`.

3. **Deploy to Apache Tomcat:**
   * Copy `target/CivicConnect.war` to your Tomcat `webapps/` directory.
   * Start Apache Tomcat using `bin/startup.bat` (Windows) or `bin/startup.sh` (Linux/Mac).

4. **Access Web Application:**
   * **Citizen Platform:** `http://localhost:8080/CivicConnect/index.html`
   * **Submit Complaint Form:** `http://localhost:8080/CivicConnect/complaint.html`
   * **Status Tracker:** `http://localhost:8080/CivicConnect/tracker.html`
   * **Admin Dashboard (MVC JSP):** `http://localhost:8080/CivicConnect/admin`
   * **WSDL Endpoint:** `http://localhost:8080/CivicConnect/WEB-INF/wsdl/DepartmentAllocationService.wsdl`

---

## 🛠️ Key Artifact Locations
* **XML Schema:** `src/main/webapp/WEB-INF/xml/complaint.xsd`
* **XSLT Stylesheet:** `src/main/webapp/WEB-INF/xml/complaint-report.xslt`
* **SOAP WSDL Contract:** `src/main/webapp/WEB-INF/wsdl/DepartmentAllocationService.wsdl`
* **SOAP Web Service SIB:** `src/main/java/com/civicconnect/soap/DepartmentAllocationServiceImpl.java`
* **Technical Academic Report:** `docs/TECHNICAL_REPORT.md`
* **Individual Reflection:** `docs/INDIVIDUAL_REFLECTION.md`
* **Individual Contribution:** `docs/INDIVIDUAL_CONTRIBUTION.md`

---

## 🧪 Running Automated Unit Tests
Execute the full JUnit 5 test suite with Maven:
```bash
mvn test
```
All tests evaluate client/server regex validation, XML/XSD schema verification, SOAP department allocation routing, session security, and XSS sanitization.
