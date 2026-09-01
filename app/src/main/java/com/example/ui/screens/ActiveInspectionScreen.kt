package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.*
import com.example.ui.components.*
import com.example.ui.theme.*

enum class ActiveInspectionTab(val title: String) {
    CHECKLIST("Checklist"),
    EVIDENCE("Evidence"),
    OBSERVATIONS("Observations"),
    VIOLATIONS("Violations"),
    REVIEW("Review & Submit")
}

@Composable
fun ActiveInspectionScreen(
    inspection: Inspection,
    currentUser: UserProfile,
    onUpdateChecklist: (itemId: String, status: ComplianceStatus, remarks: String) -> Unit,
    onAddEvidence: (checklistItemId: String?, title: String, type: String, description: String) -> Unit,
    onDeleteEvidence: (evidenceId: String) -> Unit,
    onAddObservation: (title: String, category: String, severity: Severity, description: String) -> Unit,
    onAddViolation: (type: String, severity: Severity, desc: String, item: String, action: String, deadline: String) -> Unit,
    onSubmitInspection: (finalRemarks: String, recommendation: String, signatureName: String) -> String,
    onBack: () -> Unit,
    onSubmitSuccess: (ackId: String) -> Unit
) {
    var selectedMainTab by remember { mutableStateOf(ActiveInspectionTab.CHECKLIST) }
    var selectedSectionIndex by remember { mutableStateOf(0) }

    // Dialog States
    var showCaptureEvidenceModal by remember { mutableStateOf(false) }
    var activeChecklistItemForEvidence by remember { mutableStateOf<String?>(null) }
    var showAddObservationDialog by remember { mutableStateOf(false) }
    var showAddViolationDialog by remember { mutableStateOf(false) }
    var showSubmitConfirmationDialog by remember { mutableStateOf(false) }

    // Submission Form States
    var statutoryDeclarationAgreed by remember { mutableStateOf(false) }
    var inspectorSignature by remember { mutableStateOf(currentUser.name) }
    var finalRemarks by remember { mutableStateOf(inspection.finalRemarks ?: "") }
    var selectedRecommendation by remember {
        mutableStateOf(inspection.recommendation ?: "Approval Recommended — Subject to Standard Compliance")
    }

    val sections = listOf(
        "Fire & Life Safety",
        "Structural & Exit",
        "Electrical Safety",
        "Environmental & Effluent",
        "Welfare & Labor"
    )

    val currentSection = sections.getOrElse(selectedSectionIndex) { sections.first() }
    val sectionItems = inspection.checklistItems.filter { it.section.title.contains(currentSection, ignoreCase = true) }

    val totalItems = inspection.checklistItems.size
    val answeredItems = inspection.checklistItems.count { it.status != ComplianceStatus.UNANSWERED }
    val compliantCount = inspection.checklistItems.count { it.status == ComplianceStatus.COMPLIANT }
    val partialCount = inspection.checklistItems.count { it.status == ComplianceStatus.PARTIALLY_COMPLIANT }
    val nonCompliantCount = inspection.checklistItems.count { it.status == ComplianceStatus.NON_COMPLIANT }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GovBackground)
    ) {
        // Official Header
        GovernmentHeader(
            title = "Active Field Inspection",
            subtitle = "${inspection.id} • ${inspection.siteName}",
            showBackButton = true,
            onBackClick = onBack
        )

        // Live Inspection Status & Auto-Save Subheader
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(GovNavyDark.copy(alpha = 0.95f))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF22C55E))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Live Audit: Started ${inspection.startTime ?: "10:15 AM"}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CloudDone,
                    contentDescription = null,
                    tint = Color(0xFFFDE68A),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Auto-Saved Locally",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFFFDE68A),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        // Primary Module Tabs (Checklist, Evidence, Observations, Violations, Review)
        ScrollableTabRow(
            selectedTabIndex = selectedMainTab.ordinal,
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.White,
            contentColor = GovNavyPrimary,
            edgePadding = 12.dp
        ) {
            ActiveInspectionTab.values().forEach { tab ->
                val countLabel = when (tab) {
                    ActiveInspectionTab.CHECKLIST -> "$answeredItems/$totalItems"
                    ActiveInspectionTab.EVIDENCE -> inspection.evidenceList.size.toString()
                    ActiveInspectionTab.OBSERVATIONS -> inspection.observations.size.toString()
                    ActiveInspectionTab.VIOLATIONS -> inspection.violations.size.toString()
                    ActiveInspectionTab.REVIEW -> if (answeredItems == totalItems) "Ready" else "$answeredItems/$totalItems"
                }

                Tab(
                    selected = selectedMainTab == tab,
                    onClick = { selectedMainTab = tab },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = tab.title,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (selectedMainTab == tab) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 12.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (selectedMainTab == tab) GovNavyPrimary else GovSurfaceVariant,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = countLabel,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (selectedMainTab == tab) Color.White else GovTextSecondary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp
                                    )
                                )
                            }
                        }
                    }
                )
            }
        }

        // Module Body Content
        when (selectedMainTab) {
            ActiveInspectionTab.CHECKLIST -> {
                // Section Selector Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(GovSurfaceVariant)
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    sections.forEachIndexed { idx, sectionName ->
                        val isSelected = selectedSectionIndex == idx
                        val secCount = inspection.checklistItems.filter { it.section.title.contains(sectionName, ignoreCase = true) }
                        val secDone = secCount.count { it.status != ComplianceStatus.UNANSWERED }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) GovNavyPrimary else Color.White)
                                .border(1.dp, if (isSelected) GovNavyPrimary else GovBorder, RoundedCornerShape(6.dp))
                                .clickable { selectedSectionIndex = idx }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${idx + 1}. $sectionName ($secDone/${secCount.size})",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isSelected) Color.White else GovTextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }

                // Checklist Items
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(sectionItems) { idx, item ->
                        ChecklistItemCard(
                            item = item,
                            index = idx + 1,
                            onStatusChange = { newStatus, remarks ->
                                onUpdateChecklist(item.id, newStatus, remarks)
                            },
                            onAddEvidence = {
                                activeChecklistItemForEvidence = item.id
                                showCaptureEvidenceModal = true
                            },
                            attachedEvidenceCount = inspection.evidenceList.count { it.checklistItemId == item.id }
                        )
                    }

                    item {
                        // Section Navigation Actions
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (selectedSectionIndex > 0) {
                                OutlinedButton(
                                    onClick = { selectedSectionIndex-- },
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Previous Section")
                                }
                            } else {
                                Spacer(modifier = Modifier.width(8.dp))
                            }

                            if (selectedSectionIndex < sections.size - 1) {
                                Button(
                                    onClick = { selectedSectionIndex++ },
                                    shape = RoundedCornerShape(4.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = GovNavyPrimary)
                                ) {
                                    Text("Next Section")
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                            } else {
                                Button(
                                    onClick = { selectedMainTab = ActiveInspectionTab.REVIEW },
                                    shape = RoundedCornerShape(4.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = GovIndiaGreen)
                                ) {
                                    Text("Proceed to Review")
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }

            ActiveInspectionTab.EVIDENCE -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Geotagged Field Evidence (${inspection.evidenceList.size})",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = GovNavyPrimary
                            )
                        )
                        Button(
                            onClick = {
                                activeChecklistItemForEvidence = null
                                showCaptureEvidenceModal = true
                            },
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GovNavyPrimary),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.AddAPhoto, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Capture Evidence", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (inspection.evidenceList.isEmpty()) {
                        EmptyState(
                            title = "No Evidence Attached Yet",
                            description = "Capture geotagged site photographs, documents, or equipment nameplates.",
                            icon = Icons.Default.CameraAlt,
                            actionText = "Capture First Photo",
                            onActionClick = { showCaptureEvidenceModal = true }
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(bottom = 20.dp)
                        ) {
                            itemsIndexed(inspection.evidenceList) { _, ev ->
                                EvidenceCard(
                                    evidence = ev,
                                    onDelete = { onDeleteEvidence(ev.id) }
                                )
                            }
                        }
                    }
                }
            }

            ActiveInspectionTab.OBSERVATIONS -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "General Observations (${inspection.observations.size})",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = GovNavyPrimary
                            )
                        )
                        Button(
                            onClick = { showAddObservationDialog = true },
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GovNavyPrimary),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.AddComment, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Observation", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (inspection.observations.isEmpty()) {
                        EmptyState(
                            title = "No Specific Observations Logged",
                            description = "Record general field findings, technical deviations, or noteworthy safety practices.",
                            icon = Icons.Default.EditNote,
                            actionText = "Log Observation",
                            onActionClick = { showAddObservationDialog = true }
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(bottom = 20.dp)
                        ) {
                            itemsIndexed(inspection.observations) { _, obs ->
                                ObservationCard(observation = obs)
                            }
                        }
                    }
                }
            }

            ActiveInspectionTab.VIOLATIONS -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Statutory Violations (${inspection.violations.size})",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = GovRedDanger
                            )
                        )
                        Button(
                            onClick = { showAddViolationDialog = true },
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = GovRedDanger),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Log Violation Notice", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (inspection.violations.isEmpty()) {
                        EmptyState(
                            title = "No Violations Flagged",
                            description = "No statutory notices or critical safety non-compliances logged.",
                            icon = Icons.Default.GppGood,
                            actionText = "Add Statutory Violation",
                            onActionClick = { showAddViolationDialog = true }
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(bottom = 20.dp)
                        ) {
                            itemsIndexed(inspection.violations) { _, vio ->
                                ViolationCard(violation = vio)
                            }
                        }
                    }
                }
            }

            ActiveInspectionTab.REVIEW -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Summary Matrix Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, GovBorderLight, RoundedCornerShape(8.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Verification Summary Matrix",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GovNavyPrimary,
                                    fontSize = 15.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Checklist Progress", style = MaterialTheme.typography.labelSmall.copy(color = GovTextMuted))
                                    Text("$answeredItems of $totalItems Answered", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                }
                                Column {
                                    Text("Evidence Attached", style = MaterialTheme.typography.labelSmall.copy(color = GovTextMuted))
                                    Text("${inspection.evidenceList.size} Photos / Files", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(color = GovBorderLight)
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("• $compliantCount Compliant", color = GovGreenDark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("• $partialCount Partial", color = GovAmberWarning, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("• $nonCompliantCount Non-Compliant", color = GovRedDanger, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }

                    // Statutory Recommendation Selector Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, GovBorderLight, RoundedCornerShape(8.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Official Officer Recommendation",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GovNavyPrimary,
                                    fontSize = 15.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            listOf(
                                "Approval Recommended — Standard Compliance Verified",
                                "Conditional Clearance — Minor Rectifications Mandated (15 Days)",
                                "Rectification Notice Mandated — Critical Non-Compliances Found",
                                "Cease & Desist Escalation — Severe Life Safety Violations"
                            ).forEach { recOption ->
                                val isSelected = selectedRecommendation == recOption
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isSelected) GovNavyContainer else Color.White)
                                        .border(1.dp, if (isSelected) GovNavyPrimary else GovBorderLight, RoundedCornerShape(6.dp))
                                        .clickable { selectedRecommendation = recOption }
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { selectedRecommendation = recOption },
                                        colors = RadioButtonDefaults.colors(selectedColor = GovNavyPrimary)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = recOption,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = if (isSelected) GovNavyPrimary else GovTextPrimary,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 12.sp
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Final Remarks Field
                    InputField(
                        value = finalRemarks,
                        onValueChange = { finalRemarks = it },
                        label = "Final Statutory Remarks & Directives",
                        placeholder = "Provide overarching observations and required zonal follow-up...",
                        singleLine = false,
                        minLines = 3,
                        maxLines = 5
                    )

                    // Digital Signature Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, GovBorderLight, RoundedCornerShape(8.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Statutory Oath & Digital Endorsement",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GovNavyPrimary,
                                    fontSize = 15.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.Top,
                                modifier = Modifier.clickable { statutoryDeclarationAgreed = !statutoryDeclarationAgreed }
                            ) {
                                Checkbox(
                                    checked = statutoryDeclarationAgreed,
                                    onCheckedChange = { statutoryDeclarationAgreed = it },
                                    colors = CheckboxDefaults.colors(checkedColor = GovNavyPrimary)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "I solemnly affirm under statutory penalty that I have physically conducted this field inspection at the site specified, verified evidence on ground, and that the data entered is truthful and accurate.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = GovTextSecondary,
                                        fontSize = 11.sp,
                                        lineHeight = 16.sp
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            InputField(
                                value = inspectorSignature,
                                onValueChange = { inspectorSignature = it },
                                label = "Officer Digital Sign-off Name",
                                placeholder = "Enter Full Name as per Official ID",
                                leadingIcon = Icons.Default.Draw
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    PrimaryButton(
                        text = "Submit Official Inspection Report",
                        icon = Icons.Default.Send,
                        containerColor = GovIndiaGreen,
                        enabled = statutoryDeclarationAgreed && inspectorSignature.isNotBlank(),
                        onClick = { showSubmitConfirmationDialog = true }
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }

    // Capture Evidence Modal
    CaptureEvidenceModal(
        show = showCaptureEvidenceModal,
        onDismiss = { showCaptureEvidenceModal = false },
        onEvidenceCaptured = { title, type, desc ->
            onAddEvidence(activeChecklistItemForEvidence, title, type, desc)
        }
    )

    // Add Observation Dialog
    if (showAddObservationDialog) {
        var obsTitle by remember { mutableStateOf("") }
        var obsCategory by remember { mutableStateOf("Fire Safety") }
        var obsSeverity by remember { mutableStateOf(Severity.MEDIUM) }
        var obsDesc by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { showAddObservationDialog = false }) {
            Surface(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)), color = Color.White) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Add General Observation", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GovNavyPrimary))
                    Spacer(modifier = Modifier.height(10.dp))
                    InputField(value = obsTitle, onValueChange = { obsTitle = it }, label = "Observation Title", placeholder = "e.g. Inadequate Ventilation")
                    Spacer(modifier = Modifier.height(8.dp))
                    InputField(value = obsDesc, onValueChange = { obsDesc = it }, label = "Detailed Finding", placeholder = "Enter details...", singleLine = false, minLines = 2)
                    Spacer(modifier = Modifier.height(16.dp))
                    PrimaryButton(text = "Save Observation", onClick = {
                        val t = if (obsTitle.isNotBlank()) obsTitle else "Field Finding"
                        val d = if (obsDesc.isNotBlank()) obsDesc else "Observation recorded during inspection."
                        onAddObservation(t, obsCategory, obsSeverity, d)
                        showAddObservationDialog = false
                    })
                }
            }
        }
    }

    // Add Violation Dialog
    if (showAddViolationDialog) {
        var vioType by remember { mutableStateOf("") }
        var vioSeverity by remember { mutableStateOf(Severity.HIGH) }
        var vioDesc by remember { mutableStateOf("") }
        var vioAction by remember { mutableStateOf("") }
        var vioDeadline by remember { mutableStateOf("7 Days from Notice") }

        Dialog(onDismissRequest = { showAddViolationDialog = false }) {
            Surface(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)), color = Color.White) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Log Statutory Violation Notice", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GovRedDanger))
                    Spacer(modifier = Modifier.height(10.dp))
                    InputField(value = vioType, onValueChange = { vioType = it }, label = "Violation Type", placeholder = "e.g. Blocked Fire Hydrant")
                    Spacer(modifier = Modifier.height(8.dp))
                    InputField(value = vioDesc, onValueChange = { vioDesc = it }, label = "Violation Description", placeholder = "Describe infraction...", singleLine = false, minLines = 2)
                    Spacer(modifier = Modifier.height(8.dp))
                    InputField(value = vioAction, onValueChange = { vioAction = it }, label = "Mandated Rectification Action", placeholder = "Action required...")
                    Spacer(modifier = Modifier.height(16.dp))
                    PrimaryButton(text = "Issue Violation Notice", containerColor = GovRedDanger, onClick = {
                        val t = if (vioType.isNotBlank()) vioType else "Statutory Non-Compliance"
                        val d = if (vioDesc.isNotBlank()) vioDesc else "Violation detected during audit."
                        val a = if (vioAction.isNotBlank()) vioAction else "Immediate compliance mandated."
                        onAddViolation(t, vioSeverity, d, "General", a, vioDeadline)
                        showAddViolationDialog = false
                    })
                }
            }
        }
    }

    // Submit Confirmation Dialog
    ConfirmationModal(
        show = showSubmitConfirmationDialog,
        title = "Confirm Statutory Submission",
        message = "Are you sure you want to officially file this inspection report to the DOJS central registry?\n\nOnce submitted, an official acknowledgment receipt will be generated and the report will be locked for zonal review.",
        confirmText = "Yes, Submit Officially",
        cancelText = "Review Again",
        onConfirm = {
            val ack = onSubmitInspection(finalRemarks, selectedRecommendation, inspectorSignature)
            onSubmitSuccess(ack)
        },
        onDismiss = { showSubmitConfirmationDialog = false }
    )
}
