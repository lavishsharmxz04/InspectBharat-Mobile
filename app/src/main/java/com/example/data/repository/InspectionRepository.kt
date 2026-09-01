package com.example.data.repository

import com.example.data.mock.MockData
import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.*

class InspectionRepository {

    // Auth State
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentUser = MutableStateFlow(UserProfile())
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    // Inspections
    private val _inspections = MutableStateFlow<List<Inspection>>(MockData.getInitialInspections())
    val inspections: StateFlow<List<Inspection>> = _inspections.asStateFlow()

    // Active Inspection being viewed or conducted
    private val _selectedInspectionId = MutableStateFlow<String?>("INS-2026-00124")
    val selectedInspectionId: StateFlow<String?> = _selectedInspectionId.asStateFlow()

    // Reports
    private val _reports = MutableStateFlow<List<Report>>(MockData.getInitialReports())
    val reports: StateFlow<List<Report>> = _reports.asStateFlow()

    // Selected Report for details view
    private val _selectedReportId = MutableStateFlow<String?>("REP-2026-0891")
    val selectedReportId: StateFlow<String?> = _selectedReportId.asStateFlow()

    // Notifications
    private val _notifications = MutableStateFlow<List<NotificationItem>>(MockData.getInitialNotifications())
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    // Team Members
    private val _teamMembers = MutableStateFlow<List<TeamMember>>(MockData.getTeamMembers())
    val teamMembers: StateFlow<List<TeamMember>> = _teamMembers.asStateFlow()

    // Offline / Sync System
    private val _isOfflineMode = MutableStateFlow(false)
    val isOfflineMode: StateFlow<Boolean> = _isOfflineMode.asStateFlow()

    private val _lastSyncTime = MutableStateFlow("Just now (01 Sep, 10:45 AM)")
    val lastSyncTime: StateFlow<String> = _lastSyncTime.asStateFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    // App Settings
    private val _biometricEnabled = MutableStateFlow(false)
    val biometricEnabled: StateFlow<Boolean> = _biometricEnabled.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    private val _darkModeEnabled = MutableStateFlow(false)
    val darkModeEnabled: StateFlow<Boolean> = _darkModeEnabled.asStateFlow()

    private val _autoSaveEnabled = MutableStateFlow(true)
    val autoSaveEnabled: StateFlow<Boolean> = _autoSaveEnabled.asStateFlow()

    private val _highAccuracyGps = MutableStateFlow(true)
    val highAccuracyGps: StateFlow<Boolean> = _highAccuracyGps.asStateFlow()

    private val _selectedLanguage = MutableStateFlow("English (Official)")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    // Submission acknowledgment
    private val _lastSubmittedAckId = MutableStateFlow<String?>(null)
    val lastSubmittedAckId: StateFlow<String?> = _lastSubmittedAckId.asStateFlow()

    // Authentication Actions
    fun login(username: String, password: String): Boolean {
        if ((username == "inspector.demo" && password == "123456") ||
            (username.isNotBlank() && password.length >= 6)
        ) {
            _isLoggedIn.value = true
            return true
        }
        return false
    }

    fun logout() {
        _isLoggedIn.value = false
    }

    fun selectInspection(id: String) {
        _selectedInspectionId.value = id
    }

    fun selectReport(id: String) {
        _selectedReportId.value = id
    }

    fun startInspection(inspectionId: String) {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val currentTime = timeFormat.format(Date())

        _inspections.update { list ->
            list.map { item ->
                if (item.id == inspectionId) {
                    val checklist = if (item.checklistItems.isEmpty()) MockData.createStandardChecklist() else item.checklistItems
                    item.copy(
                        status = InspectionStatus.IN_PROGRESS,
                        startTime = item.startTime ?: currentTime,
                        checklistItems = checklist
                    )
                } else item
            }
        }
        _selectedInspectionId.value = inspectionId
    }

    fun updateChecklistItem(
        inspectionId: String,
        itemId: String,
        newStatus: ComplianceStatus,
        remarks: String
    ) {
        _inspections.update { list ->
            list.map { insp ->
                if (insp.id == inspectionId) {
                    val updatedItems = insp.checklistItems.map { chk ->
                        if (chk.id == itemId) {
                            chk.copy(status = newStatus, remarks = remarks)
                        } else chk
                    }
                    insp.copy(checklistItems = updatedItems)
                } else insp
            }
        }
    }

    fun addEvidence(
        inspectionId: String,
        checklistItemId: String?,
        title: String,
        type: String,
        description: String,
        location: String = "Okhla Ph-III, New Delhi",
        coordinates: String = "28.5355° N, 77.2690° E"
    ): Evidence {
        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val newEvidence = Evidence(
            id = "EVD-2026-" + String.format(Locale.US, "%04d", (100..999).random()),
            inspectionId = inspectionId,
            checklistItemId = checklistItemId,
            title = title,
            type = type,
            timestamp = dateFormat.format(Date()),
            location = location,
            coordinates = coordinates,
            description = description
        )

        _inspections.update { list ->
            list.map { insp ->
                if (insp.id == inspectionId) {
                    val updatedEvidence = insp.evidenceList + newEvidence
                    val updatedChecklist = if (checklistItemId != null) {
                        insp.checklistItems.map { chk ->
                            if (chk.id == checklistItemId) {
                                chk.copy(attachedEvidenceIds = chk.attachedEvidenceIds + newEvidence.id)
                            } else chk
                        }
                    } else insp.checklistItems

                    insp.copy(
                        evidenceList = updatedEvidence,
                        checklistItems = updatedChecklist
                    )
                } else insp
            }
        }
        return newEvidence
    }

    fun deleteEvidence(inspectionId: String, evidenceId: String) {
        _inspections.update { list ->
            list.map { insp ->
                if (insp.id == inspectionId) {
                    insp.copy(
                        evidenceList = insp.evidenceList.filterNot { it.id == evidenceId },
                        checklistItems = insp.checklistItems.map { chk ->
                            chk.copy(attachedEvidenceIds = chk.attachedEvidenceIds.filterNot { it == evidenceId })
                        }
                    )
                } else insp
            }
        }
    }

    fun addObservation(
        inspectionId: String,
        title: String,
        category: String,
        severity: Severity,
        description: String,
        linkedEvidenceIds: List<String> = emptyList()
    ) {
        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val newObservation = Observation(
            id = "OBS-2026-" + String.format(Locale.US, "%03d", (10..99).random()),
            inspectionId = inspectionId,
            title = title,
            category = category,
            severity = severity,
            description = description,
            timestamp = dateFormat.format(Date()),
            linkedEvidenceIds = linkedEvidenceIds
        )

        _inspections.update { list ->
            list.map { insp ->
                if (insp.id == inspectionId) {
                    insp.copy(observations = insp.observations + newObservation)
                } else insp
            }
        }
    }

    fun addViolation(
        inspectionId: String,
        violationType: String,
        severity: Severity,
        description: String,
        relevantChecklistItem: String,
        recommendedAction: String,
        correctiveDeadline: String,
        linkedEvidenceIds: List<String> = emptyList()
    ) {
        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val newViolation = Violation(
            id = "VIO-2026-" + String.format(Locale.US, "%03d", (10..99).random()),
            inspectionId = inspectionId,
            violationType = violationType,
            severity = severity,
            description = description,
            relevantChecklistItem = relevantChecklistItem,
            recommendedAction = recommendedAction,
            correctiveDeadline = correctiveDeadline,
            timestamp = dateFormat.format(Date()),
            linkedEvidenceIds = linkedEvidenceIds
        )

        _inspections.update { list ->
            list.map { insp ->
                if (insp.id == inspectionId) {
                    insp.copy(violations = insp.violations + newViolation)
                } else insp
            }
        }
    }

    fun submitInspection(
        inspectionId: String,
        finalRemarks: String,
        recommendation: String,
        signatureName: String
    ): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val now = dateFormat.format(Date())
        val ackNumber = "ACK-DOJS-" + String.format(Locale.US, "%06d", (100000..999999).random())

        var submittedInspection: Inspection? = null

        _inspections.update { list ->
            list.map { insp ->
                if (insp.id == inspectionId) {
                    val updated = insp.copy(
                        status = InspectionStatus.SUBMITTED,
                        finalRemarks = finalRemarks,
                        recommendation = recommendation,
                        digitalSignature = signatureName,
                        submissionTimestamp = now,
                        ackId = ackNumber
                    )
                    submittedInspection = updated
                    updated
                } else insp
            }
        }

        // Generate corresponding Report entry
        submittedInspection?.let { insp ->
            val compliantCount = insp.checklistItems.count { it.status == ComplianceStatus.COMPLIANT }
            val partialCount = insp.checklistItems.count { it.status == ComplianceStatus.PARTIALLY_COMPLIANT }
            val nonCompliantCount = insp.checklistItems.count { it.status == ComplianceStatus.NON_COMPLIANT }

            val newReport = Report(
                id = "REP-2026-" + String.format(Locale.US, "%04d", (900..999).random()),
                inspectionId = insp.id,
                siteName = insp.siteName,
                organizationName = insp.organizationName,
                location = insp.fullAddress.substringBefore(","),
                inspectionDate = insp.scheduledDate,
                submissionDate = now,
                inspectorName = _currentUser.value.name,
                status = "Submitted",
                compliantCount = compliantCount,
                partialCount = partialCount,
                nonCompliantCount = nonCompliantCount,
                violationsCount = insp.violations.size,
                observationsCount = insp.observations.size,
                recommendation = recommendation
            )

            _reports.update { listOf(newReport) + it }
            _selectedReportId.value = newReport.id

            // Add notification
            val notif = NotificationItem(
                id = "NOTIF-" + System.currentTimeMillis().toString().takeLast(4),
                title = "Inspection Submitted ($ackNumber)",
                message = "Statutory report for ${insp.siteName} has been officially recorded and queued for DOJS zonal review.",
                timestamp = "Just now",
                type = NotificationType.APPROVED,
                isRead = false,
                relatedInspectionId = insp.id
            )
            _notifications.update { listOf(notif) + it }
        }

        _lastSubmittedAckId.value = ackNumber
        return ackNumber
    }

    fun markNotificationRead(id: String) {
        _notifications.update { list ->
            list.map { if (it.id == id) it.copy(isRead = true) else it }
        }
    }

    fun markAllNotificationsRead() {
        _notifications.update { list ->
            list.map { it.copy(isRead = true) }
        }
    }

    fun toggleOfflineMode() {
        _isOfflineMode.update { !it }
    }

    fun triggerSync() {
        val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        _lastSyncTime.value = "Just now (${dateFormat.format(Date())})"
    }

    fun toggleBiometric(value: Boolean) { _biometricEnabled.value = value }
    fun toggleNotifications(value: Boolean) { _notificationsEnabled.value = value }
    fun toggleDarkMode(value: Boolean) { _darkModeEnabled.value = value }
    fun toggleAutoSave(value: Boolean) { _autoSaveEnabled.value = value }
    fun toggleHighAccuracyGps(value: Boolean) { _highAccuracyGps.value = value }
    fun setLanguage(lang: String) { _selectedLanguage.value = lang }
}
