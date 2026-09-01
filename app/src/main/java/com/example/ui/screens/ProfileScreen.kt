package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserProfile
import com.example.ui.components.ConfirmationModal
import com.example.ui.components.GovernmentHeader
import com.example.ui.components.SecondaryButton
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    user: UserProfile,
    onNavigateTeam: () -> Unit,
    onNavigateSettings: () -> Unit,
    onLogout: () -> Unit,
    onBack: () -> Unit
) {
    var showLogoutModal by remember { mutableStateOf(false) }
    var showConductModal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GovBackground)
    ) {
        GovernmentHeader(
            title = "Officer Profile & Credentials",
            subtitle = "Authorized Inspection Directorate Record",
            showBackButton = true,
            onBackClick = onBack
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Official Identity Credential Card
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
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(GovNavyPrimary)
                                    .border(2.dp, GovAmberWarning, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = user.name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString(""),
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = user.name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = GovNavyPrimary,
                                        fontSize = 17.sp
                                    )
                                )
                                Text(
                                    text = user.designation,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = GovTextSecondary,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 12.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .background(GovNavyContainer, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "ID: ${user.employeeId} • Badge: ${user.badgeNumber}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = GovNavyPrimary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = GovBorderLight)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Organization & Jurisdiction
                        ProfileDetailRow(label = "Department", value = user.organization)
                        ProfileDetailRow(label = "Jurisdiction", value = user.jurisdiction)
                        ProfileDetailRow(label = "Security Clearance", value = user.securityClearance)
                        ProfileDetailRow(label = "Official Email", value = user.email)
                        ProfileDetailRow(label = "Mobile Contact", value = user.phone)
                    }
                }
            }

            // Quick Inspection Performance Metrics
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, GovBorderLight, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Statutory Service Statistics (FY 2026)",
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
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("48", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = GovNavyPrimary))
                                Text("Audits Filed", style = MaterialTheme.typography.labelSmall.copy(color = GovTextSecondary, fontSize = 11.sp))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("99.4%", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = GovIndiaGreen))
                                Text("Compliance QA", style = MaterialTheme.typography.labelSmall.copy(color = GovTextSecondary, fontSize = 11.sp))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("18", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = GovAmberWarning))
                                Text("Notices Issued", style = MaterialTheme.typography.labelSmall.copy(color = GovTextSecondary, fontSize = 11.sp))
                            }
                        }
                    }
                }
            }

            // Navigation Actions Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, GovBorderLight, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column {
                        ProfileMenuActionItem(
                            icon = Icons.Default.Groups,
                            title = "Field Inspection Team Directory",
                            subtitle = "View active team members and technical specialists",
                            onClick = onNavigateTeam
                        )
                        HorizontalDivider(color = GovBorderLight)
                        ProfileMenuActionItem(
                            icon = Icons.Default.Settings,
                            title = "Application & Security Settings",
                            subtitle = "Configure biometrics, sync, dark mode, GPS",
                            onClick = onNavigateSettings
                        )
                        HorizontalDivider(color = GovBorderLight)
                        ProfileMenuActionItem(
                            icon = Icons.Default.Gavel,
                            title = "Statutory Code of Conduct",
                            subtitle = "Review official field inspector guidelines and ethics",
                            onClick = { showConductModal = true }
                        )
                    }
                }
            }

            // Logout Button
            item {
                SecondaryButton(
                    text = "Sign Out of Officer Session",
                    icon = Icons.Default.Logout,
                    borderColor = GovRedDanger,
                    textColor = GovRedDanger,
                    onClick = { showLogoutModal = true }
                )
            }
        }
    }

    // Logout Confirmation Modal
    ConfirmationModal(
        show = showLogoutModal,
        title = "Confirm Session Logout",
        message = "Are you sure you want to sign out? Unsynchronized local records have been preserved and will be securely re-loaded on your next login.",
        confirmText = "Sign Out",
        cancelText = "Cancel",
        isDestructive = true,
        onConfirm = onLogout,
        onDismiss = { showLogoutModal = false }
    )

    // Code of Conduct Modal
    ConfirmationModal(
        show = showConductModal,
        title = "Statutory Code of Conduct",
        message = "1. Integrity: Inspections must be conducted impartially.\n2. Timeliness: Findings must be filed within 24 hours of field visit.\n3. Evidence: All violations require geotagged photographic proof.\n4. Confidentiality: Inspection logs are official government property.",
        confirmText = "Understood",
        cancelText = "Close",
        onConfirm = { showConductModal = false },
        onDismiss = { showConductModal = false }
    )
}

@Composable
private fun ProfileDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(color = GovTextMuted, fontSize = 12.sp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = GovTextPrimary, fontSize = 12.sp)
        )
    }
}

@Composable
private fun ProfileMenuActionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(GovNavyContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = GovNavyPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = GovTextPrimary,
                    fontSize = 13.sp
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = GovTextSecondary,
                    fontSize = 11.sp
                )
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = GovTextMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}
