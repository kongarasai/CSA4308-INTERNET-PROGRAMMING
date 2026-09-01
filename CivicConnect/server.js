const http = require('http');
const fs = require('fs');
const path = require('path');
const url = require('url');
const querystring = require('querystring');

const PORT = 8080;
const WEBAPP_DIR = path.join(__dirname, 'src', 'main', 'webapp');

// In-Memory Data Store (mirroring ComplaintDataStore.java)
let complaintCounter = 1004;
const complaints = [
  {
    complaintId: "CMP1001",
    citizenId: "C101",
    citizenName: "Rajesh Kumar",
    category: "POTHOLE",
    description: "Deep pothole on Main Street near Sector 4 intersection causing traffic congestion and safety hazards.",
    location: "Main Street, Sector 4, MG Road",
    pincode: "560001",
    timestamp: new Date().toISOString(),
    status: "SUBMITTED",
    department: "Roads & Infrastructure",
    sla: "24 Hours",
    priority: "HIGH"
  },
  {
    complaintId: "CMP1002",
    citizenId: "C101",
    citizenName: "Rajesh Kumar",
    category: "GARBAGE_OVERFLOW",
    description: "Unattended garbage bin overflowing near market entry creating unhygienic conditions.",
    location: "Commercial Market Complex, Block B",
    pincode: "560002",
    timestamp: new Date().toISOString(),
    status: "IN_PROGRESS",
    department: "Sanitation & Waste Management",
    sla: "12 Hours",
    priority: "HIGH"
  },
  {
    complaintId: "CMP1003",
    citizenId: "C102",
    citizenName: "Priya Sharma",
    category: "BROKEN_STREETLIGHT",
    description: "Streetlight non-functional for 3 consecutive nights creating poor visibility.",
    location: "12th Cross Road, Indiranagar",
    pincode: "560038",
    timestamp: new Date().toISOString(),
    status: "RESOLVED",
    department: "Electricity & Street Lighting",
    sla: "36 Hours",
    priority: "MEDIUM"
  },
  {
    complaintId: "CMP1004",
    citizenId: "C103",
    citizenName: "Anil Mehta",
    category: "WATER_LEAKAGE",
    description: "Major water pipe burst on 5th Main Road leading to water wastage.",
    location: "5th Main Road, Jayanagar",
    pincode: "560041",
    timestamp: new Date().toISOString(),
    status: "IN_PROGRESS",
    department: "Water Supply & Sewerage Board",
    sla: "18 Hours",
    priority: "CRITICAL"
  }
];

const users = [
  { id: "C101", name: "Rajesh Kumar", email: "rajesh@example.com", password: "password123", pincode: "560001" },
  { id: "ADMIN1", name: "Municipal Admin", email: "admin@civicconnect.gov.in", password: "admin123", pincode: "560001" }
];

// SOAP Routing Logic Matrix (mirroring DepartmentAllocationServiceImpl.java)
function allocateDepartment(category, location) {
  const cat = (category || 'GENERAL').trim().toUpperCase();
  switch (cat) {
    case 'POTHOLE':
    case 'ROADS':
      return { department: 'Roads & Infrastructure', sla: '24 Hours', priority: 'HIGH' };
    case 'GARBAGE_OVERFLOW':
    case 'GARBAGE':
    case 'SANITATION':
      return { department: 'Sanitation & Waste Management', sla: '12 Hours', priority: 'HIGH' };
    case 'BROKEN_STREETLIGHT':
    case 'STREETLIGHT':
    case 'ELECTRICITY':
      return { department: 'Electricity & Street Lighting', sla: '36 Hours', priority: 'MEDIUM' };
    case 'WATER_LEAKAGE':
    case 'WATER':
      return { department: 'Water Supply & Sewerage Board', sla: '18 Hours', priority: 'CRITICAL' };
    default:
      return { department: 'General Municipal Administration', sla: '48 Hours', priority: 'LOW' };
  }
}

// Generate XSLT-like HTML report grouping complaints by department
function generateXSLTReportHTML() {
  const grouped = {};
  complaints.forEach(c => {
    if (!grouped[c.department]) grouped[c.department] = [];
    grouped[c.department].push(c);
  });

  let html = `<div class="xslt-report-container" style="font-family: inherit;">
    <div style="background: rgba(99, 102, 241, 0.1); border-left: 4px solid var(--primary-color, #6366f1); padding: 1rem; border-radius: 8px; margin-bottom: 1.5rem;">
      <h3 style="margin: 0 0 0.5rem 0; color: var(--text-primary, #f8fafc);">📄 Dynamic XSLT Transformation Preview</h3>
      <p style="margin: 0; font-size: 0.85rem; color: var(--text-secondary, #94a3b8);">
        Generated dynamically by transforming <code>complaints.xml</code> with <code>complaint-report.xslt</code> rules.
      </p>
    </div>`;

  for (const dept in grouped) {
    html += `
    <div style="background: var(--card-bg, #1e293b); border: 1px solid var(--border-color, #334155); border-radius: 12px; padding: 1.2rem; margin-bottom: 1.5rem;">
      <h4 style="margin: 0 0 1rem 0; font-size: 1.1rem; color: var(--primary-color, #818cf8); display: flex; align-items: center; justify-content: space-between;">
        <span>🏢 Department: ${dept}</span>
        <span class="badge" style="background: rgba(99,102,241,0.2); color: #818cf8;">${grouped[dept].length} Active Complaint(s)</span>
      </h4>
      <div class="table-responsive">
        <table class="data-table" style="width:100%; border-collapse: collapse; font-size: 0.85rem;">
          <thead>
            <tr style="border-bottom: 2px solid var(--border-color, #334155); text-align: left;">
              <th style="padding: 0.5rem;">Complaint ID</th>
              <th style="padding: 0.5rem;">Category</th>
              <th style="padding: 0.5rem;">Location</th>
              <th style="padding: 0.5rem;">SLA</th>
              <th style="padding: 0.5rem;">Priority</th>
              <th style="padding: 0.5rem;">Status</th>
            </tr>
          </thead>
          <tbody>`;

    grouped[dept].forEach(c => {
      html += `
            <tr style="border-bottom: 1px solid var(--border-color, #334155);">
              <td style="padding: 0.6rem; font-weight: bold;">${c.complaintId}</td>
              <td style="padding: 0.6rem;"><span class="tag">${c.category}</span></td>
              <td style="padding: 0.6rem;">${c.location}</td>
              <td style="padding: 0.6rem;"><span class="badge sla-badge">${c.sla}</span></td>
              <td style="padding: 0.6rem;"><span class="badge priority-${c.priority.toLowerCase()}">${c.priority}</span></td>
              <td style="padding: 0.6rem;"><span class="badge status-badge ${c.status.toLowerCase()}">${c.status}</span></td>
            </tr>`;
    });

    html += `
          </tbody>
        </table>
      </div>
    </div>`;
  }

  html += `</div>`;
  return html;
}

// MIME types mapping
const MIME_TYPES = {
  '.html': 'text/html; charset=UTF-8',
  '.jsp': 'text/html; charset=UTF-8',
  '.css': 'text/css',
  '.js': 'application/javascript',
  '.json': 'application/json',
  '.xml': 'application/xml',
  '.xsd': 'text/xml',
  '.xslt': 'text/xml',
  '.wsdl': 'application/wsdl+xml',
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.svg': 'image/svg+xml',
  '.ico': 'image/x-icon'
};

const server = http.createServer((req, res) => {
  const parsedUrl = url.parse(req.url, true);
  let pathname = parsedUrl.pathname;

  // Handle API & Servlet Routes
  if (req.method === 'POST' && (pathname === '/api/complaints' || pathname === '/submitComplaint')) {
    let body = '';
    req.on('data', chunk => { body += chunk.toString(); });
    req.on('end', () => {
      let params = querystring.parse(body);
      if (req.headers['content-type'] && req.headers['content-type'].includes('application/json')) {
        try { params = JSON.parse(body); } catch(e) {}
      }

      const category = (params.category || '').trim();
      const description = (params.description || '').trim();
      const location = (params.location || '').trim();
      const pincode = (params.pincode || '').trim();

      if (!category || !description || !location) {
        res.writeHead(400, { 'Content-Type': 'application/json' });
        return res.end(JSON.stringify({ success: false, message: 'Category, description, and location are required fields.' }));
      }

      if (pincode && !/^[1-9][0-9]{5}$/.test(pincode)) {
        res.writeHead(400, { 'Content-Type': 'application/json' });
        return res.end(JSON.stringify({ success: false, message: 'Invalid Indian PIN code format.' }));
      }

      complaintCounter++;
      const complaintId = 'CMP' + complaintCounter;
      const timestamp = new Date().toISOString();
      const status = 'SUBMITTED';

      const allocation = allocateDepartment(category, location);

      const newComplaint = {
        complaintId,
        citizenId: 'C101',
        citizenName: 'Rajesh Kumar',
        category,
        description,
        location,
        pincode,
        timestamp,
        status,
        department: allocation.department,
        sla: allocation.sla,
        priority: allocation.priority
      };

      complaints.unshift(newComplaint);

      res.writeHead(201, { 'Content-Type': 'application/json' });
      return res.end(JSON.stringify({
        success: true,
        message: 'Complaint submitted and automatically routed via SOAP service successfully.',
        complaintId,
        category,
        description,
        location,
        pincode,
        timestamp,
        status,
        department: allocation.department,
        sla: allocation.sla,
        priority: allocation.priority
      }));
    });
    return;
  }

  if (req.method === 'GET' && (pathname === '/api/status' || pathname === '/checkStatus')) {
    const id = (parsedUrl.query.id || '').trim();
    if (!id) {
      res.writeHead(400, { 'Content-Type': 'application/json' });
      return res.end(JSON.stringify({ success: false, message: 'Complaint ID is required.' }));
    }

    const found = complaints.find(c => c.complaintId.toLowerCase() === id.toLowerCase());
    if (found) {
      res.writeHead(200, { 'Content-Type': 'application/json' });
      return res.end(JSON.stringify({
        success: true,
        complaintId: found.complaintId,
        category: found.category,
        description: found.description,
        location: found.location,
        status: found.status,
        department: found.department,
        sla: found.sla,
        timestamp: found.timestamp
      }));
    } else {
      res.writeHead(404, { 'Content-Type': 'application/json' });
      return res.end(JSON.stringify({ success: false, message: `No complaint found matching ID: ${id}` }));
    }
  }

  if (req.method === 'GET' && (pathname === '/api/complaints' || pathname === '/api/complaint-list')) {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    return res.end(JSON.stringify({ success: true, count: complaints.length, complaints }));
  }

  if (req.method === 'POST' && (pathname === '/api/login' || pathname === '/login')) {
    let body = '';
    req.on('data', chunk => { body += chunk.toString(); });
    req.on('end', () => {
      const params = querystring.parse(body);
      const email = (params.email || '').toLowerCase().trim();
      const password = (params.password || '').trim();

      const user = users.find(u => u.email.toLowerCase() === email && u.password === password);
      if (user) {
        res.writeHead(200, { 'Content-Type': 'application/json' });
        return res.end(JSON.stringify({ success: true, message: 'Login successful', redirect: email.includes('admin') ? 'admin' : 'index.html' }));
      } else {
        res.writeHead(401, { 'Content-Type': 'application/json' });
        return res.end(JSON.stringify({ success: false, message: 'Invalid email or password.' }));
      }
    });
    return;
  }

  if (req.method === 'POST' && (pathname === '/api/register' || pathname === '/register')) {
    let body = '';
    req.on('data', chunk => { body += chunk.toString(); });
    req.on('end', () => {
      const params = querystring.parse(body);
      const name = (params.name || '').trim();
      const email = (params.email || '').trim();
      const password = (params.password || '').trim();
      const pincode = (params.pincode || '').trim();

      if (!name || !email || !password) {
        res.writeHead(400, { 'Content-Type': 'application/json' });
        return res.end(JSON.stringify({ success: false, message: 'Name, email, and password are required.' }));
      }

      users.push({ id: 'C' + (users.length + 100), name, email, password, pincode });
      res.writeHead(201, { 'Content-Type': 'application/json' });
      return res.end(JSON.stringify({ success: true, message: 'Registration successful! You can now log in.', redirect: 'login.html' }));
    });
    return;
  }

  if (pathname === '/api/logout') {
    res.writeHead(302, { Location: '/login.html' });
    return res.end();
  }

  // Handle Admin Dashboard & XSLT Report Pages (Translating JSP templates to dynamic HTML)
  if (pathname === '/admin' || pathname === '/admin-dashboard.jsp') {
    fs.readFile(path.join(WEBAPP_DIR, 'admin-dashboard.jsp'), 'utf8', (err, template) => {
      if (err) {
        res.writeHead(500, { 'Content-Type': 'text/plain' });
        return res.end('Error reading admin dashboard JSP');
      }

      // Generate complaint rows HTML
      let rowsHTML = complaints.map(c => `
        <tr>
          <td><strong>${c.complaintId}</strong></td>
          <td>${c.citizenName} (${c.citizenId})</td>
          <td><span class="tag">${c.category}</span></td>
          <td style="max-width: 240px;">${c.description}</td>
          <td>${c.location}</td>
          <td><strong>${c.department}</strong></td>
          <td><span class="badge sla-badge">${c.sla}</span></td>
          <td><span class="badge status-badge ${c.status.toLowerCase()}">${c.status}</span></td>
        </tr>
      `).join('');

      const xsltHTML = generateXSLTReportHTML();

      // Replace JSP tags with actual rendered data
      let rendered = template
        .replace(/<%@ taglib.*%>/g, '')
        .replace(/<%@ page.*%>/g, '')
        .replace(/<c:out value="\${totalCount}" default="0"\/>/g, complaints.length.toString())
        .replace(/<c:choose>[\s\S]*?<\/c:choose>/g, rowsHTML)
        .replace(/\${xsltReport}/g, xsltHTML);

      res.writeHead(200, { 'Content-Type': 'text/html; charset=UTF-8' });
      return res.end(rendered);
    });
    return;
  }

  if (pathname === '/complaint-report.jsp' || pathname === '/admin/report') {
    fs.readFile(path.join(WEBAPP_DIR, 'complaint-report.jsp'), 'utf8', (err, template) => {
      if (err) {
        res.writeHead(500, { 'Content-Type': 'text/plain' });
        return res.end('Error reading complaint report JSP');
      }

      const xsltHTML = generateXSLTReportHTML();

      let rendered = template
        .replace(/<%@ taglib.*%>/g, '')
        .replace(/<%@ page.*%>/g, '')
        .replace(/<c:out value="\${xsltReport}" escapeXml="false"\/>/g, xsltHTML);

      res.writeHead(200, { 'Content-Type': 'text/html; charset=UTF-8' });
      return res.end(rendered);
    });
    return;
  }

  // WSDL Endpoint mapping
  if (pathname === '/ws/departmentAllocation' || pathname === '/DepartmentAllocationService') {
    const wsdlPath = path.join(WEBAPP_DIR, 'WEB-INF', 'wsdl', 'DepartmentAllocationService.wsdl');
    fs.readFile(wsdlPath, (err, data) => {
      if (err) {
        res.writeHead(404, { 'Content-Type': 'text/plain' });
        return res.end('WSDL not found');
      }
      res.writeHead(200, { 'Content-Type': 'application/wsdl+xml; charset=UTF-8' });
      return res.end(data);
    });
    return;
  }

  // Serve static webapp files
  if (pathname === '/') pathname = '/index.html';
  const filePath = path.join(WEBAPP_DIR, pathname);

  fs.stat(filePath, (err, stats) => {
    if (err || !stats.isFile()) {
      const errorPage = path.join(WEBAPP_DIR, 'error.html');
      fs.readFile(errorPage, (err2, data) => {
        res.writeHead(404, { 'Content-Type': 'text/html; charset=UTF-8' });
        return res.end(data || '<h1>404 Not Found</h1>');
      });
      return;
    }

    const ext = path.extname(filePath).toLowerCase();
    const contentType = MIME_TYPES[ext] || 'application/octet-stream';

    fs.readFile(filePath, (err, data) => {
      if (err) {
        res.writeHead(500, { 'Content-Type': 'text/plain' });
        return res.end('Internal Server Error');
      }
      res.writeHead(200, { 'Content-Type': contentType });
      res.end(data);
    });
  });
});

server.listen(PORT, () => {
  console.log(`=======================================================`);
  console.log(`🏛️  CivicConnect Platform Live Dev Server Running`);
  console.log(`-------------------------------------------------------`);
  console.log(`🌐 Citizen Portal:       http://localhost:${PORT}/index.html`);
  console.log(`📝 Submit Complaint:     http://localhost:${PORT}/complaint.html`);
  console.log(`🔍 Status Tracker:       http://localhost:${PORT}/tracker.html`);
  console.log(`👑 Admin Dashboard:      http://localhost:${PORT}/admin`);
  console.log(`📊 XSLT Report:          http://localhost:${PORT}/complaint-report.jsp`);
  console.log(`⚡ SOAP WSDL Endpoint:   http://localhost:${PORT}/ws/departmentAllocation?wsdl`);
  console.log(`=======================================================`);
});
