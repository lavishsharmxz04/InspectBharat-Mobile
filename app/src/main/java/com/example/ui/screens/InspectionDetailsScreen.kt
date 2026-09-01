package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ComplianceStatus
import com.example.data.model.Inspection
import com.example.data.model.InspectionStatus
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun InspectionDetailsScreen(
    inspection: Inspection,
    onStartOrResumeInspection: (String) -> Unit,
    onViewReport: (String) -> Unit,
    onBack: () -> Unit
) {
    var showMapInfoDialog by remember { mutableStateOf(false) }

    val totalItems = inspection.checklistItems.size
    val compliantCount = inspection.checklistItems.count { it.status == ComplianceStatus.COMPLIANT }
    val partialCount = inspection.checklistItems.count { it.status == ComplianceStatus.PARTIALLY_COMPLIANT }
    val nonCompliantCount = inspection.checklistItems.count { it.status == ComplianceStatus.NON_COMPLIANT }
    val answeredCount = totalItems - inspection.checklistItems.count { it.status == ComplianceStatus.UNANSWERED }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GovBackground)
    ) {
        GovernmentHeader(
            title = "Inspection Overview",
            subtitle = inspection.id,
            showBackButton = true,
            onBackClick = onBack
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, GovBorderLight, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .background(GovNavyPrimary, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = inspection.id,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                PriorityBadge(priority = inspection.priority)
                            }
                            StatusBadge(status = inspection.status)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = inspection.siteName,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = GovTextPrimary,
                                fontSize = 18.sp
                            )
                        )
                        Text(
                            text = inspection.organizationName,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = GovNavyPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = GovBorderLight, thickness = 0.8.dp)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Date & Time
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Scheduled Date",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = GovTextMuted,
                                        fontSize = 11.sp
                                    )
                                )
                                Text(
                                    text = inspection.scheduledDate,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = GovTextPrimary,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp
                                    )
                                )
                            }
                            Column {
                                Text(
                                    text = "Reporting Slot",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = GovTextMuted,
                                        fontSize = 11.sp
                                    )
                                )
                                Text(
                                    text = inspection.scheduledTime,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = GovTextPrimary,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Facility Location & Contact Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, GovBorderLight, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Site Location & Authorized Contact",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = GovNavyPrimary,
                                fontSize = 14.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = GovNavyPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = inspection.fullAddress,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = GovTextPrimary,
                                        fontSize = 13.sp,
                                        lineHeight = 18.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "GPS Coordinates: ${inspection.latitude}° N, ${inspection.longitude}° E",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = GovTextMuted,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Simulated Map Container
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    Brush.linearGradient(listOf(GovNavyDark, Color(0xFF1E293B)))
                                )
                                .border(1.dp, GovBorder, RoundedCornerShape(6.dp))
                                .clickable { showMapInfoDialog = true }
                                .padding(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "GOVERNMENT GIS GEO-FENCE",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFFFF9933),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        )
                                    )
                                    Text(
                                        text = "ZONE: ${inspection.zone}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color.White,
                                            fontSize = 9.sp
                                        )
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Place,
                                        contentDescription = null,
                                        tint = Color(0xFFEF4444),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = inspection.siteName,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    )
                                }

                                Text(
                                    text = "Tap to open official GIS geo-coordinates",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF94A3B8),
                                        fontSize = 9.sp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Contact Person Details
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(GovSurfaceVariant, RoundedCornerShape(6.dp))
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(GovNavyPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = inspection.contactPerson,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = GovTextPrimary,
                                        fontSize = 13.sp
                                    )
                                )
                                Text(
                                    text = "${inspection.contactPhone} • ${inspection.contactEmail}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = GovTextSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Scope & Statutory Act Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, GovBorderLight, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Scope of Statutory Inspection",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = GovNavyPrimary,
                                fontSize = 14.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Inspection Type: ${inspection.inspectionType}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = GovTextPrimary,
                                fontSize = 13.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Mandate Act: ${inspection.mandateAct}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = GovTextSecondary,
                                fontSize = 12.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(GovNavyContainer, RoundedCornerShape(6.dp))
                                .padding(10.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Special Officer Instructions:",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = GovNavyPrimary,
                                        fontSize = 11.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = inspection.instructions,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = GovNavyPrimary,
                                        fontSize = 11.sp,
                                        lineHeight = 16.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Checklist & Progress Preview Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, GovBorderLight, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Statutory Checklist Verification",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GovNavyPrimary,
                                    fontSize = 14.sp
                                )
                            )
                            Text(
                                text = "$answeredCount / $totalItems Answered",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (answeredCount == totalItems && totalItems > 0) GovGreenDark else GovAmberWarning
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Compliance progress bar
                        LinearProgressIndicator(
                            progress = { if (totalItems > 0) answeredCount.toFloat() / totalItems else 0f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = GovNavyPrimary,
                            trackColor = GovBorderLight
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "$compliantCount Compliant",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = GovGreenDark,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                            Text(
                                text = "$partialCount Partial",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = GovAmberWarning,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                            Text(
                                text = "$nonCompliantCount Non-Compliant",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = GovRedDanger,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = GovNavyPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${inspection.evidenceList.size} Geotagged Evidence Attachments Recorded",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = GovTextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // Bottom Action Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (inspection.status == InspectionStatus.COMPLETED || inspection.status == InspectionStatus.SUBMITTED) {
                    PrimaryButton(
                        text = "View Statutory Inspection Report",
                        icon = Icons.Default.Description,
                        containerColor = GovIndiaGreen,
                        onClick = { onViewReport(inspection.id) }
                    )
                } else {
                    PrimaryButton(
                        text = if (inspection.status == InspectionStatus.IN_PROGRESS) "Resume Inspection (${inspection.id})" else "Begin Official Inspection",
                        icon = Icons.Default.PlayArrow,
                        onClick = { onStartOrResumeInspection(inspection.id) }
                    )
                }
            }
        }
    }

    // Map Coordinates Dialog
    ConfirmationModal(
        show = showMapInfoDialog,
        title = "Statutory Site Geolocation",
        message = "Official Coordinates:\nLatitude: ${inspection.latitude}° N\nLongitude: ${inspection.longitude}° E\nJurisdiction Zone: ${inspection.zone}\n\nSite is within the designated official GIS inspection geofence (Accuracy: ±2.1m).",
        confirmText = "Got it",
        cancelText = "Dismiss",
        onConfirm = { showMapInfoDialog = false },
        onDismiss = { showMapInfoDialog = false }
    )
}
