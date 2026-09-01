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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NotificationItem
import com.example.data.model.NotificationType
import com.example.ui.components.EmptyState
import com.example.ui.components.GovernmentHeader
import com.example.ui.theme.*

@Composable
fun NotificationsScreen(
    notifications: List<NotificationItem>,
    onMarkRead: (String) -> Unit,
    onMarkAllRead: () -> Unit,
    onSelectInspection: (String) -> Unit,
    onBack: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf<String>("ALL") }

    val filteredList = remember(notifications, selectedFilter) {
        when (selectedFilter) {
            "UNREAD" -> notifications.filter { !it.isRead }
            "URGENT" -> notifications.filter { it.type == NotificationType.URGENT }
            else -> notifications
        }
    }

    val unreadCount = notifications.count { !it.isRead }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GovBackground)
    ) {
        GovernmentHeader(
            title = "Official Alerts & Notices",
            subtitle = "$unreadCount Unread Statutory Communications",
            showBackButton = true,
            onBackClick = onBack
        )

        // Filter Bar & Mark All Read
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(
                    "ALL" to "All (${notifications.size})",
                    "UNREAD" to "Unread ($unreadCount)",
                    "URGENT" to "Urgent"
                ).forEach { (filterKey, label) ->
                    val isSelected = selectedFilter == filterKey
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isSelected) GovNavyPrimary else Color.White)
                            .border(1.dp, if (isSelected) GovNavyPrimary else GovBorder, RoundedCornerShape(4.dp))
                            .clickable { selectedFilter = filterKey }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isSelected) Color.White else GovTextSecondary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            if (unreadCount > 0) {
                TextButton(
                    onClick = onMarkAllRead,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Mark all as read",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = GovNavyPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        if (filteredList.isEmpty()) {
            EmptyState(
                title = "No Notifications",
                description = "You have no active statutory alerts or directives at this time.",
                icon = Icons.Default.NotificationsNone
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList) { item ->
                    NotificationCard(
                        item = item,
                        onClick = {
                            onMarkRead(item.id)
                            if (item.relatedInspectionId != null) {
                                onSelectInspection(item.relatedInspectionId)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationCard(
    item: NotificationItem,
    onClick: () -> Unit
) {
    val (icon, iconColor, iconBg) = when (item.type) {
        NotificationType.URGENT, NotificationType.CRITICAL_ALERT -> Triple(Icons.Default.PriorityHigh, GovRedDanger, GovRedLight)
        NotificationType.ASSIGNMENT, NotificationType.NEW_ASSIGNMENT, NotificationType.DUE_TOMORROW -> Triple(Icons.Default.Assignment, GovNavyPrimary, GovNavyContainer)
        NotificationType.APPROVED -> Triple(Icons.Default.CheckCircle, GovIndiaGreen, GovGreenLight)
        NotificationType.RETURNED -> Triple(Icons.Default.AssignmentReturn, GovAmberWarning, GovAmberLight)
        NotificationType.SYNC -> Triple(Icons.Default.Sync, GovNavyPrimary, GovNavyContainer)
        NotificationType.SYSTEM -> Triple(Icons.Default.Info, GovTextSecondary, GovSurfaceVariant)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(
                1.dp,
                if (!item.isRead) GovNavyPrimary.copy(alpha = 0.4f) else GovBorderLight,
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (!item.isRead) Color(0xFFF8FAFC) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = if (!item.isRead) FontWeight.Bold else FontWeight.SemiBold,
                            color = GovTextPrimary,
                            fontSize = 13.sp
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    if (!item.isRead) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(GovRedDanger)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.message,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = GovTextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.timestamp,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = GovTextMuted,
                            fontSize = 10.sp
                        )
                    )

                    if (item.relatedInspectionId != null) {
                        Text(
                            text = "View ${item.relatedInspectionId} →",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GovNavyPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
