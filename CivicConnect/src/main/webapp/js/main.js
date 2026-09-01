/**
 * CivicConnect Main JavaScript Script
 * Implements: Vanilla JS DOM Scripting, Client-side Regex Validation, AJAX (Fetch API), 
 * Dynamic DOM Node Insertion, and Dark-Mode Toggle.
 * Fulfills CO2 requirements.
 */

document.addEventListener('DOMContentLoaded', () => {
  initDarkMode();
  initFormValidations();
  initComplaintTracker();
});

/* ==========================================================================
   1. DARK MODE TOGGLE (LocalStorage Persistence)
   ========================================================================== */
function initDarkMode() {
  const toggleBtn = document.getElementById('dark-mode-toggle');
  if (!toggleBtn) return;

  const currentTheme = localStorage.getItem('theme');
  if (currentTheme === 'dark') {
    document.body.classList.add('dark-mode');
    toggleBtn.innerHTML = '☀️ Light Mode';
  }

  toggleBtn.addEventListener('click', () => {
    document.body.classList.toggle('dark-mode');
    const isDark = document.body.classList.contains('dark-mode');
    localStorage.setItem('theme', isDark ? 'dark' : 'light');
    toggleBtn.innerHTML = isDark ? '☀️ Light Mode' : '🌙 Dark Mode';
  });
}

/* ==========================================================================
   2. CLIENT-SIDE REGEX VALIDATION & AJAX FORM SUBMISSION
   ========================================================================== */
function initFormValidations() {
  const complaintForm = document.getElementById('complaint-form');
  if (!complaintForm) return;

  // Regular Expression Definitions
  const mobileRegex = /^[6-9]\d{9}$/;      // Indian 10-digit mobile number starting with 6-9
  const pincodeRegex = /^[1-9][0-9]{5}$/;  // Indian 6-digit PIN code

  complaintForm.addEventListener('submit', async (event) => {
    event.preventDefault();

    let isValid = true;

    // Element references
    const categorySelect = document.getElementById('category');
    const descriptionText = document.getElementById('description');
    const locationInput = document.getElementById('location');
    const pincodeInput = document.getElementById('pincode');
    const mobileInput = document.getElementById('mobile');
    const responseAlert = document.getElementById('form-response-alert');

    // Reset previous errors
    clearFormErrors(complaintForm);

    // Validate Category
    if (!categorySelect.value || categorySelect.value.trim() === '') {
      showError('category-group', 'Please select a valid complaint category.');
      isValid = false;
    }

    // Validate Description
    if (!descriptionText.value || descriptionText.value.trim().length < 5) {
      showError('description-group', 'Description must be at least 5 characters long.');
      isValid = false;
    }

    // Validate Location
    if (!locationInput.value || locationInput.value.trim() === '') {
      showError('location-group', 'Location is required.');
      isValid = false;
    }

    // Validate Pincode via Regex
    if (pincodeInput && pincodeInput.value) {
      if (!pincodeRegex.test(pincodeInput.value.trim())) {
        showError('pincode-group', 'Invalid PIN code. Must be a valid 6-digit Indian PIN code.');
        isValid = false;
      }
    }

    // Validate Mobile via Regex (if present)
    if (mobileInput && mobileInput.value) {
      if (!mobileRegex.test(mobileInput.value.trim())) {
        showError('mobile-group', 'Invalid mobile number. Must be 10 digits starting with 6, 7, 8, or 9.');
        isValid = false;
      }
    }

    if (!isValid) return;

    // Construct FormData / URLSearchParams payload for Servlet POST
    const formData = new URLSearchParams();
    formData.append('category', categorySelect.value);
    formData.append('description', descriptionText.value);
    formData.append('location', locationInput.value);
    if (pincodeInput) formData.append('pincode', pincodeInput.value);

    // Submit via AJAX Fetch API
    try {
      showLoading(true);

      const response = await fetch('api/complaints', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
        },
        body: formData.toString()
      });

      const data = await response.json();
      showLoading(false);

      if (response.ok && data.success) {
        // Display Success Alert
        if (responseAlert) {
          responseAlert.className = 'alert alert-success animated-entry';
          responseAlert.innerHTML = `
            <strong>✅ Complaint Submitted Successfully!</strong><br>
            <strong>Complaint ID:</strong> ${data.complaintId}<br>
            <strong>Allocated Department (via SOAP Service):</strong> ${data.department}<br>
            <strong>Estimated SLA:</strong> ${data.sla}
          `;
          responseAlert.style.display = 'block';
        }

        // Dynamically insert new complaint entry into Tracker DOM without page refresh
        dynamicallyInsertComplaintNode(data);

        // Reset Form
        complaintForm.reset();

      } else {
        if (responseAlert) {
          responseAlert.className = 'alert alert-error animated-entry';
          responseAlert.innerHTML = `<strong>⚠️ Error:</strong> ${data.message || 'Failed to submit complaint.'}`;
          responseAlert.style.display = 'block';
        }
      }
    } catch (error) {
      showLoading(false);
      console.error('AJAX Submission Error:', error);
      if (responseAlert) {
        responseAlert.className = 'alert alert-error animated-entry';
        responseAlert.innerHTML = `<strong>⚠️ Network Error:</strong> Unable to connect to server backend.`;
        responseAlert.style.display = 'block';
      }
    }
  });
}

function showError(groupId, message) {
  const group = document.getElementById(groupId);
  if (group) {
    group.classList.add('has-error');
    const errText = group.querySelector('.error-text');
    if (errText) {
      errText.textContent = message;
      errText.style.display = 'block';
    }
  }
}

function clearFormErrors(form) {
  const groups = form.querySelectorAll('.form-group');
  groups.forEach(g => {
    g.classList.remove('has-error');
    const errText = g.querySelector('.error-text');
    if (errText) errText.style.display = 'none';
  });
  const alert = document.getElementById('form-response-alert');
  if (alert) alert.style.display = 'none';
}

function showLoading(isLoading) {
  const submitBtn = document.getElementById('submit-btn');
  if (submitBtn) {
    submitBtn.disabled = isLoading;
    submitBtn.innerHTML = isLoading ? '⏳ Processing & Routing via SOAP...' : 'Submit Complaint';
  }
}

/* ==========================================================================
   3. DYNAMIC DOM NODE CREATION & TRACKER UPDATES (CO2 & CO3)
   ========================================================================== */
function dynamicallyInsertComplaintNode(complaint) {
  const trackerTableBody = document.getElementById('tracker-tbody');
  if (!trackerTableBody) return;

  // Create table row element using DOM APIs
  const tr = document.createElement('tr');
  tr.className = 'animated-entry newly-updated';

  tr.innerHTML = `
    <td><strong>${escapeHTML(complaint.complaintId)}</strong></td>
    <td><span class="tag">${escapeHTML(complaint.category)}</span></td>
    <td>${escapeHTML(complaint.location)}</td>
    <td>${escapeHTML(complaint.department)}</td>
    <td><span class="badge sla-badge">${escapeHTML(complaint.sla)}</span></td>
    <td><span class="badge status-badge ${complaint.status.toLowerCase()}">${escapeHTML(complaint.status)}</span></td>
    <td><button class="btn btn-outline" style="padding: 0.2rem 0.5rem; font-size: 0.8rem;" onclick="viewDetails('${complaint.complaintId}')">Details</button></td>
  `;

  // Insert as first child in tracker table
  if (trackerTableBody.firstChild) {
    trackerTableBody.insertBefore(tr, trackerTableBody.firstChild);
  } else {
    trackerTableBody.appendChild(tr);
  }
}

function initComplaintTracker() {
  const searchBtn = document.getElementById('status-search-btn');
  const searchInput = document.getElementById('status-search-input');
  if (!searchBtn || !searchInput) return;

  searchBtn.addEventListener('click', async () => {
    const id = searchInput.value.trim();
    if (!id) return;

    try {
      const res = await fetch(`api/status?id=${encodeURIComponent(id)}`);
      const data = await res.json();
      const resultDiv = document.getElementById('status-search-result');
      
      if (res.ok && data.success) {
        resultDiv.className = 'alert alert-success animated-entry';
        resultDiv.innerHTML = `
          <h4>Complaint #${escapeHTML(data.complaintId)}</h4>
          <p><strong>Category:</strong> ${escapeHTML(data.category)}</p>
          <p><strong>Department:</strong> ${escapeHTML(data.department)}</p>
          <p><strong>Resolution SLA:</strong> ${escapeHTML(data.sla)}</p>
          <p><strong>Current Status:</strong> <span class="badge status-badge ${data.status.toLowerCase()}">${escapeHTML(data.status)}</span></p>
          <p><strong>Location:</strong> ${escapeHTML(data.location)}</p>
        `;
        resultDiv.style.display = 'block';
      } else {
        resultDiv.className = 'alert alert-error animated-entry';
        resultDiv.innerHTML = `⚠️ ${escapeHTML(data.message || 'Complaint ID not found.')}`;
        resultDiv.style.display = 'block';
      }
    } catch (e) {
      console.error('Status fetch error:', e);
    }
  });
}

function escapeHTML(str) {
  if (!str) return '';
  return str.replace(/[&<>'"]/g, 
    tag => ({
      '&': '&amp;',
      '<': '&lt;',
      '>': '&gt;',
      "'": '&#39;',
      '"': '&quot;'
    }[tag] || tag)
  );
}
