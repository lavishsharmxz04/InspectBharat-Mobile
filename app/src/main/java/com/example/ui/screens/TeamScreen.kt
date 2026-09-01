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
import com.example.data.model.TeamMember
import com.example.ui.components.ConfirmationModal
import com.example.ui.components.GovernmentHeader
import com.example.ui.theme.*

@Composable
fun TeamScreen(
    teamMembers: List<TeamMember>,
    onBack: () -> Unit
) {
    var selectedContactMember by remember { mutableStateOf<TeamMember?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GovBackground)
    ) {
        GovernmentHeader(
            title = "Field Inspection Team",
            subtitle = "Taskforce Roster & Specialists Directory",
            showBackButton = true,
            onBackClick = onBack
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Active Zonal Inspection Officers (${teamMembers.size})",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = GovTextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            items(teamMembers) { member ->
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
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(GovNavyPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = member.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
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
                                        text = member.name,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = GovNavyPrimary,
                                            fontSize = 14.sp
                                        )
                                    )

                                    Box(
                                        modifier = Modifier
                                            .background(GovNavyContainer, RoundedCornerShape(4.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = member.role,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = GovNavyPrimary,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp
                                            )
                                        )
                                    }
                                }

                                Text(
                                    text = "${member.designation} • ID: ${member.id}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = GovTextSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = GovBorderLight, thickness = 0.8.dp)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Jurisdiction & Audits",
                                    style = MaterialTheme.typography.labelSmall.copy(color = GovTextMuted, fontSize = 10.sp)
                                )
                                Text(
                                    text = "${member.assignedZone} • ${member.completedInspections} Audits Done",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = GovTextPrimary,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                IconButton(
                                    onClick = { selectedContactMember = member },
                                    modifier = Modifier
                                        .size(34.dp)
                                        .background(GovSurfaceVariant, RoundedCornerShape(4.dp))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = "Call",
                                        tint = GovNavyPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                IconButton(
                                    onClick = { selectedContactMember = member },
                                    modifier = Modifier
                                        .size(34.dp)
                                        .background(GovSurfaceVariant, RoundedCornerShape(4.dp))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = "Email",
                                        tint = GovNavyPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (selectedContactMember != null) {
        ConfirmationModal(
            show = true,
            title = "Official Officer Contact",
            message = "Officer: ${selectedContactMember?.name}\nRole: ${selectedContactMember?.role}\nDesignation: ${selectedContactMember?.designation}\nPhone: ${selectedContactMember?.phone}\nEmail: ${selectedContactMember?.email}\nZone: ${selectedContactMember?.assignedZone}",
            confirmText = "Done",
            cancelText = "Dismiss",
            onConfirm = { selectedContactMember = null },
            onDismiss = { selectedContactMember = null }
        )
    }
}
