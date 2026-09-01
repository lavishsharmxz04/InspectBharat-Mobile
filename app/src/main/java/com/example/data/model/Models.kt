package com.example.data.model

enum class InspectionStatus(val label: String) {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    SUBMITTED("Submitted")
}

enum class Priority(val label: String) {
    HIGH("High Priority"),
    MEDIUM("Medium Priority"),
    LOW("Low Priority")
}

enum class ComplianceStatus(val label: String) {
    UNANSWERED("Not Inspected"),
    COMPLIANT("Compliant"),
    PARTIALLY_COMPLIANT("Partially Compliant"),
    NON_COMPLIANT("Non-Compliant"),
    NOT_APPLICABLE("Not Applicable")
}

enum class Severity(val label: String) {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    CRITICAL("Critical")
}

enum class ChecklistSection(val id: Int, val title: String, val subtitle: String) {
    BASIC_VERIFICATION(1, "Basic Verification", "Site identification & statutory registration"),
    SITE_INFRASTRUCTURE(2, "Site Infrastructure", "Physical perimeter, access & structural safety"),
    SAFETY_COMPLIANCE(3, "Safety Compliance", "Fire, electrical, PPE & emergency exits"),
    DOCUMENTATION(4, "Documentation", "Licenses, safety logs & statutory registers"),
    FINAL_ASSESSMENT(5, "Final Assessment", "Concluding remarks, officer sign-off & scoring")
}

data class ChecklistItem(
    val id: String,
    val section: ChecklistSection,
    val question: String,
    val statutoryReference: String = "DOJS/GIMS-STD/2026",
    val isEvidenceRequired: Boolean = false,
    var status: ComplianceStatus = ComplianceStatus.UNANSWERED,
    var remarks: String = "",
    var attachedEvidenceIds: List<String> = emptyList()
)

data class Evidence(
    val id: String,
    val inspectionId: String,
    val checklistItemId: String? = null,
    val title: String,
    val type: String = "PHOTO", // PHOTO, DOCUMENT, NOTE
    val timestamp: String,
    val location: String,
    val coordinates: String,
    val description: String,
    val thumbnailKey: String = "field_capture"
)

data class Observation(
    val id: String,
    val inspectionId: String,
    val title: String,
    val category: String, // Safety, Infrastructure, Documentation, Compliance, Other
    val severity: Severity,
    val description: String,
    val timestamp: String,
    val linkedEvidenceIds: List<String> = emptyList()
)

data class Violation(
    val id: String,
    val inspectionId: String,
    val violationType: String,
    val severity: Severity,
    val description: String,
    val relevantChecklistItem: String,
    val recommendedAction: String,
    val correctiveDeadline: String,
    val timestamp: String,
    val linkedEvidenceIds: List<String> = emptyList()
)

data class Inspection(
    val id: String,
    val siteName: String,
    val organizationName: String,
    val inspectionType: String, // Periodic Statutory Compliance, Structural Audit, Environmental & Fire Safety, etc.
    val fullAddress: String,
    val coordinates: String,
    val latitude: Double = 28.5355,
    val longitude: Double = 77.2690,
    val zone: String = "Zone-II (North NCR)",
    val contactPerson: String = "Sunil Verma (Site In-Charge)",
    val contactPhone: String = "+91 98112 34567",
    val contactEmail: String = "site.compliance@infra.org",
    val mandateAct: String = "Section 14(B) National Infrastructure & Safety Act",
    val scheduledDate: String,
    val scheduledTime: String,
    val priority: Priority,
    var status: InspectionStatus,
    val assignedTeam: String = "Central Inspection Unit #04",
    val assignedInspector: String = "Rajesh Sharma",
    val instructions: String,
    val specialNotes: String = "Ensure verification of Fire NOC renewed within current fiscal year. Geotagged photographic evidence mandatory.",
    var startTime: String? = null,
    var endTime: String? = null,
    var checklistItems: List<ChecklistItem> = emptyList(),
    var observations: List<Observation> = emptyList(),
    var violations: List<Violation> = emptyList(),
    var evidenceList: List<Evidence> = emptyList(),
    var finalRemarks: String = "",
    var recommendation: String = "Statutory compliance verified with noted observations. Timely compliance required for pending rectifications.",
    var digitalSignature: String = "",
    var submissionTimestamp: String? = null,
    var ackId: String? = null
)

data class Report(
    val id: String,
    val inspectionId: String,
    val siteName: String,
    val organizationName: String,
    val location: String,
    val inspectionDate: String,
    val submissionDate: String,
    val inspectorName: String,
    val status: String, // Submitted, Approved, Returned, Draft
    val compliantCount: Int,
    val partialCount: Int,
    val nonCompliantCount: Int,
    val violationsCount: Int,
    val observationsCount: Int,
    val recommendation: String
)

enum class NotificationType {
    NEW_ASSIGNMENT,
    DUE_TOMORROW,
    RETURNED,
    APPROVED,
    CRITICAL_ALERT,
    URGENT,
    ASSIGNMENT,
    SYNC,
    SYSTEM
}

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val type: NotificationType,
    var isRead: Boolean = false,
    val relatedInspectionId: String? = null
)

data class TeamMember(
    val id: String,
    val name: String,
    val designation: String,
    val role: String, // Team Leader, Field Inspector, Technical Inspector, Documentation Officer
    val status: String, // Active, On Field, Off Duty
    val phone: String,
    val email: String,
    val assignedZone: String = "Zone-II (North NCR)",
    val completedInspections: Int
)

data class UserProfile(
    val name: String = "Rajesh Sharma",
    val designation: String = "Senior Field Inspection Officer",
    val organization: String = "National Infrastructure & Safety Directorate",
    val department: String = "DOJS Field Monitoring Wing",
    val employeeId: String = "GOI-DOJS-8924-ND",
    val badgeNumber: String = "INSP-DL-0824",
    val securityClearance: String = "Level 2 (Statutory Inspector)",
    val email: String = "inspector.demo@dojs.gov.in",
    val phone: String = "+91 98765 43210",
    val jurisdiction: String = "National Capital Region (Zone-II)",
    val accountStatus: String = "Active & Authorized"
)
