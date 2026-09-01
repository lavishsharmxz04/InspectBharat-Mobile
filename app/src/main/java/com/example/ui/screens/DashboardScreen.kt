package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.Inspection
import com.example.data.model.InspectionStatus
import com.example.data.model.UserProfile
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    user: UserProfile,
    inspections: List<Inspection>,
    unreadNotificationCount: Int,
    isOffline: Boolean,
    lastSyncTime: String,
    onToggleOffline: () -> Unit,
    onSyncClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit,
    onViewAllInspections: (filterStatus: InspectionStatus?) -> Unit,
    onViewReports: () -> Unit,
    onSelectInspection: (String) -> Unit,
    onStartInspection: (String) -> Unit
) {
    val assignedCount = inspections.size
    val pendingCount = inspections.count { it.status == InspectionStatus.PENDING }
    val inProgressCount = inspections.count { it.status == InspectionStatus.IN_PROGRESS }
    val completedCount = inspections.count { it.status == InspectionStatus.COMPLETED || it.status == InspectionStatus.SUBMITTED }

    val todayInspections = inspections.take(4)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GovBackground)
    ) {
        GovernmentHeader(
            title = "GIMS Field Portal",
            subtitle = user.organization,
            unreadNotificationCount = unreadNotificationCount,
            onNotificationClick = onNotificationClick,
            onProfileClick = onProfileClick,
            onSyncClick = onSyncClick
        )

        OfflineBanner(
            isOffline = isOffline,
            onToggleOffline = onToggleOffline
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Inspector Welcome Banner Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, GovBorderLight, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Good Morning, Officer",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = GovAmberWarning,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = user.name,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = GovNavyPrimary,
                                        fontSize = 18.sp
                                    )
                                )
                                Text(
                                    text = "${user.designation} • ID: ${user.employeeId}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = GovTextSecondary,
                                        fontSize = 12.sp
                                    )
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(GovNavyPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = user.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = GovBorderLight, thickness = 0.8.dp)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.GpsFixed,
                                    contentDescription = null,
                                    tint = GovIndiaGreen,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = user.jurisdiction,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = GovTextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }

                            SyncIndicator(
                                lastSyncTime = lastSyncTime,
                                onSyncClick = onSyncClick
                            )
                        }
                    }
                }
            }

            // Statistics Grid (Assigned, Pending, In Progress, Completed)
            item {
                Text(
                    text = "Inspection Overview",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = GovNavyPrimary,
                        fontSize = 14.sp
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatCard(
                            title = "Assigned",
                            count = assignedCount,
                            icon = Icons.Default.Assignment,
                            accentColor = GovNavyPrimary,
                            modifier = Modifier.weight(1f),
                            onClick = { onViewAllInspections(null) }
                        )
                        StatCard(
                            title = "Pending",
                            count = pendingCount,
                            icon = Icons.Default.HourglassEmpty,
                            accentColor = GovAmberWarning,
                            modifier = Modifier.weight(1f),
                            onClick = { onViewAllInspections(InspectionStatus.PENDING) }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StatCard(
                            title = "In Progress",
                            count = inProgressCount,
                            icon = Icons.Default.PlayCircleOutline,
                            accentColor = Color(0xFF0284C7),
                            modifier = Modifier.weight(1f),
                            onClick = { onViewAllInspections(InspectionStatus.IN_PROGRESS) }
                        )
                        StatCard(
                            title = "Completed",
                            count = completedCount,
                            icon = Icons.Default.CheckCircleOutline,
                            accentColor = GovIndiaGreen,
                            modifier = Modifier.weight(1f),
                            onClick = { onViewAllInspections(InspectionStatus.COMPLETED) }
                        )
                    }
                }
            }

            // Quick Actions Bar
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, GovBorderLight, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Quick Actions",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = GovNavyPrimary,
                                fontSize = 13.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            QuickActionItem(
                                title = "Start Audit",
                                icon = Icons.Default.PlayArrow,
                                color = GovNavyPrimary,
                                onClick = {
                                    val target = inspections.firstOrNull { it.status == InspectionStatus.IN_PROGRESS }
                                        ?: inspections.firstOrNull { it.status == InspectionStatus.PENDING }
                                    target?.let { onStartInspection(it.id) }
                                }
                            )
                            QuickActionItem(
                                title = "My List",
                                icon = Icons.Default.FormatListBulleted,
                                color = GovAmberWarning,
                                onClick = { onViewAllInspections(null) }
                            )
                            QuickActionItem(
                                title = "Reports",
                                icon = Icons.Default.Assessment,
                                color = GovIndiaGreen,
                                onClick = onViewReports
                            )
                            QuickActionItem(
                                title = "Alerts",
                                icon = Icons.Default.Notifications,
                                color = GovRedDanger,
                                onClick = onNotificationClick
                            )
                        }
                    }
                }
            }

            // Today's Scheduled Inspections
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Today's Assigned Inspections",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = GovNavyPrimary,
                            fontSize = 14.sp
                        )
                    )
                    TextButton(
                        onClick = { onViewAllInspections(null) },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "View All (${inspections.size})",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GovNavyPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            items(todayInspections) { item ->
                InspectionCard(
                    inspection = item,
                    onViewDetails = { onSelectInspection(item.id) },
                    onStartOrResume = { onStartInspection(item.id) }
                )
            }
        }
    }
}

@Composable
private fun QuickActionItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = GovTextPrimary,
                fontSize = 11.sp
            )
        )
    }
}
