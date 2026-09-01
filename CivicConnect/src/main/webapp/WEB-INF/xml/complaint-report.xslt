<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" indent="yes" encoding="UTF-8"/>

    <!-- Key for grouping complaints by department -->
    <xsl:key name="complaints-by-department" match="complaint" use="department"/>

    <xsl:template match="/">
        <div class="xslt-report-container">
            <header class="xslt-report-header">
                <h2>Municipal Department Complaint Allocation Report (XSLT Formatted)</h2>
                <p>Real-time automated grouping of citizen grievances by responsible municipal department.</p>
            </header>

            <!-- Grouping Loop by Department -->
            <xsl:for-each select="complaints/complaint[generate-id() = generate-id(key('complaints-by-department', department)[1])]">
                <xsl:sort select="department" order="ascending"/>
                <div class="department-group-card">
                    <div class="department-group-title">
                        <span class="dept-icon">🏛️</span>
                        <h3><xsl:value-of select="department"/></h3>
                        <span class="badge count-badge">
                            <xsl:value-of select="count(key('complaints-by-department', department))"/> Complaint(s)
                        </span>
                    </div>

                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Category</th>
                                <th>Citizen</th>
                                <th>Location</th>
                                <th>Description</th>
                                <th>SLA</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <xsl:for-each select="key('complaints-by-department', department)">
                                <tr>
                                    <td><strong><xsl:value-of select="complaintId"/></strong></td>
                                    <td><span class="tag"><xsl:value-of select="category"/></span></td>
                                    <td><xsl:value-of select="citizenName"/></td>
                                    <td><xsl:value-of select="location"/></td>
                                    <td class="desc-cell"><xsl:value-of select="description"/></td>
                                    <td><span class="badge sla-badge"><xsl:value-of select="sla"/></span></td>
                                    <td>
                                        <xsl:choose>
                                            <xsl:when test="status='SUBMITTED'">
                                                <span class="status-badge submitted">SUBMITTED</span>
                                            </xsl:when>
                                            <xsl:when test="status='ASSIGNED'">
                                                <span class="status-badge assigned">ASSIGNED</span>
                                            </xsl:when>
                                            <xsl:when test="status='IN_PROGRESS'">
                                                <span class="status-badge in-progress">IN PROGRESS</span>
                                            </xsl:when>
                                            <xsl:otherwise>
                                                <span class="status-badge resolved"><xsl:value-of select="status"/></span>
                                            </xsl:otherwise>
                                        </xsl:choose>
                                    </td>
                                </tr>
                            </xsl:for-each>
                        </tbody>
                    </table>
                </div>
            </xsl:for-each>
        </div>
    </xsl:template>

</xsl:stylesheet>
