package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.model.Report
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun ReportsScreen(
    reports: List<Report>,
    onSelectReport: (String) -> Unit,
    onBack: () -> Unit
) {
    var selectedStatusTab by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var actionDialogMessage by remember { mutableStateOf<String?>(null) }

    val filteredReports = remember(reports, selectedStatusTab, searchQuery) {
        reports.filter { item ->
            val matchesStatus = selectedStatusTab == null || item.status.equals(selectedStatusTab, ignoreCase = true)
            val matchesSearch = searchQuery.isBlank() ||
                    item.id.contains(searchQuery, ignoreCase = true) ||
                    item.inspectionId.contains(searchQuery, ignoreCase = true) ||
                    item.siteName.contains(searchQuery, ignoreCase = true) ||
                    item.organizationName.contains(searchQuery, ignoreCase = true) ||
                    item.location.contains(searchQuery, ignoreCase = true)

            matchesStatus && matchesSearch
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GovBackground)
    ) {
        GovernmentHeader(
            title = "Statutory Reports",
            subtitle = "Field Inspection Archival & Verification",
            showBackButton = true,
            onBackClick = onBack
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Search Bar
            AppSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                placeholder = "Search by Report ID, Site, or Location..."
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Status Tabs (All, Approved, Submitted, Draft, Returned)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ReportFilterChip(
                    label = "All (${reports.size})",
                    isSelected = selectedStatusTab == null,
                    onClick = { selectedStatusTab = null }
                )
                ReportFilterChip(
                    label = "Approved (${reports.count { it.status == "Approved" }})",
                    isSelected = selectedStatusTab == "Approved",
                    onClick = { selectedStatusTab = "Approved" }
                )
                ReportFilterChip(
                    label = "Submitted (${reports.count { it.status == "Submitted" }})",
                    isSelected = selectedStatusTab == "Submitted",
                    onClick = { selectedStatusTab = "Submitted" }
                )
                ReportFilterChip(
                    label = "Draft (${reports.count { it.status == "Draft" }})",
                    isSelected = selectedStatusTab == "Draft",
                    onClick = { selectedStatusTab = "Draft" }
                )
                ReportFilterChip(
                    label = "Returned (${reports.count { it.status == "Returned" }})",
                    isSelected = selectedStatusTab == "Returned",
                    onClick = { selectedStatusTab = "Returned" }
                )
            }
        }

        if (filteredReports.isEmpty()) {
            EmptyState(
                title = "No Reports Found",
                description = "No statutory reports match your filter criteria.",
                icon = Icons.Default.Description,
                actionText = "Reset Filters",
                onActionClick = {
                    searchQuery = ""
                    selectedStatusTab = null
                }
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Archived & Active Statutory Filings (${filteredReports.size})",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = GovTextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                items(filteredReports) { report ->
                    ReportCard(
                        report = report,
                        onView = { onSelectReport(report.id) },
                        onDownload = {
                            actionDialogMessage = "PDF for ${report.id} generated and exported successfully to official device downloads folder."
                        },
                        onShare = {
                            actionDialogMessage = "Statutory encrypted link for ${report.id} copied to clipboard for official transmission."
                        }
                    )
                }
            }
        }
    }

    // PDF / Share feedback modal
    ConfirmationModal(
        show = actionDialogMessage != null,
        title = "Official Action Confirmation",
        message = actionDialogMessage ?: "",
        confirmText = "Acknowledge",
        cancelText = "Close",
        onConfirm = { actionDialogMessage = null },
        onDismiss = { actionDialogMessage = null }
    )
}

@Composable
private fun ReportFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) GovNavyPrimary else Color.White)
            .border(1.dp, if (isSelected) GovNavyPrimary else GovBorder, RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 7.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = if (isSelected) Color.White else GovTextSecondary,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 12.sp
            )
        )
    }
}
