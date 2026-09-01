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
import com.example.data.model.Inspection
import com.example.data.model.InspectionStatus
import com.example.data.model.Priority
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun InspectionsScreen(
    inspections: List<Inspection>,
    initialStatusFilter: InspectionStatus? = null,
    onSelectInspection: (String) -> Unit,
    onStartInspection: (String) -> Unit,
    onBack: () -> Unit
) {
    var selectedStatusTab by remember(initialStatusFilter) { mutableStateOf(initialStatusFilter) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf<Priority?>(null) }
    var showFilterSheet by remember { mutableStateOf(false) }

    val filteredInspections = remember(inspections, selectedStatusTab, searchQuery, selectedPriority) {
        inspections.filter { item ->
            val matchesStatus = selectedStatusTab == null || item.status == selectedStatusTab
            val matchesPriority = selectedPriority == null || item.priority == selectedPriority
            val matchesSearch = searchQuery.isBlank() ||
                    item.id.contains(searchQuery, ignoreCase = true) ||
                    item.siteName.contains(searchQuery, ignoreCase = true) ||
                    item.organizationName.contains(searchQuery, ignoreCase = true) ||
                    item.fullAddress.contains(searchQuery, ignoreCase = true) ||
                    item.inspectionType.contains(searchQuery, ignoreCase = true)

            matchesStatus && matchesPriority && matchesSearch
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GovBackground)
    ) {
        GovernmentHeader(
            title = "Field Inspections",
            subtitle = "Statutory Roster & Verification Schedules",
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
                placeholder = "Search site, inspection ID, or address...",
                onFilterClick = { showFilterSheet = true },
                isFilterActive = selectedPriority != null
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Status Filter Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                StatusFilterChip(
                    label = "All (${inspections.size})",
                    isSelected = selectedStatusTab == null,
                    onClick = { selectedStatusTab = null }
                )
                StatusFilterChip(
                    label = "Pending (${inspections.count { it.status == InspectionStatus.PENDING }})",
                    isSelected = selectedStatusTab == InspectionStatus.PENDING,
                    onClick = { selectedStatusTab = InspectionStatus.PENDING }
                )
                StatusFilterChip(
                    label = "In Progress (${inspections.count { it.status == InspectionStatus.IN_PROGRESS }})",
                    isSelected = selectedStatusTab == InspectionStatus.IN_PROGRESS,
                    onClick = { selectedStatusTab = InspectionStatus.IN_PROGRESS }
                )
                StatusFilterChip(
                    label = "Completed (${inspections.count { it.status == InspectionStatus.COMPLETED || it.status == InspectionStatus.SUBMITTED }})",
                    isSelected = selectedStatusTab == InspectionStatus.COMPLETED,
                    onClick = { selectedStatusTab = InspectionStatus.COMPLETED }
                )
            }

            // Priority Filter indicator if active
            if (selectedPriority != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Filter: ",
                            style = MaterialTheme.typography.labelSmall.copy(color = GovTextSecondary)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(GovNavyContainer)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${selectedPriority?.label} Priority",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = GovNavyPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    TextButton(
                        onClick = { selectedPriority = null },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Clear Filter",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GovRedDanger,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }

        // List
        if (filteredInspections.isEmpty()) {
            EmptyState(
                title = "No Inspections Found",
                description = "No inspection records match your selected criteria or search query.",
                actionText = "Reset All Filters",
                onActionClick = {
                    searchQuery = ""
                    selectedStatusTab = null
                    selectedPriority = null
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
                        text = "Showing ${filteredInspections.size} Statutory Records",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = GovTextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                items(filteredInspections) { item ->
                    InspectionCard(
                        inspection = item,
                        onViewDetails = { onSelectInspection(item.id) },
                        onStartOrResume = { onStartInspection(item.id) }
                    )
                }
            }
        }
    }

    // Priority Filter Dialog
    if (showFilterSheet) {
        AlertDialog(
            onDismissRequest = { showFilterSheet = false },
            title = {
                Text(
                    text = "Filter by Priority",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = GovNavyPrimary
                    )
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(
                        null to "All Priorities",
                        Priority.HIGH to "High Priority (Statutory Mandate)",
                        Priority.MEDIUM to "Medium Priority (Routine Follow-up)",
                        Priority.LOW to "Low Priority (Baseline Audit)"
                    ).forEach { (prio, label) ->
                        val isSelected = selectedPriority == prio
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) GovNavyContainer else GovSurfaceVariant)
                                .clickable {
                                    selectedPriority = prio
                                    showFilterSheet = false
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    selectedPriority = prio
                                    showFilterSheet = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = GovNavyPrimary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = label,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = if (isSelected) GovNavyPrimary else GovTextPrimary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 13.sp
                                )
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showFilterSheet = false }) {
                    Text("Close", color = GovNavyPrimary)
                }
            },
            containerColor = Color.White
        )
    }
}

@Composable
private fun StatusFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) GovNavyPrimary else Color.White)
            .border(
                1.dp,
                if (isSelected) GovNavyPrimary else GovBorder,
                RoundedCornerShape(6.dp)
            )
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
