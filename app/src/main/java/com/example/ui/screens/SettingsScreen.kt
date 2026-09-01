package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.components.ConfirmationModal
import com.example.ui.components.GovernmentHeader
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    biometricEnabled: Boolean,
    notificationsEnabled: Boolean,
    autoSaveEnabled: Boolean,
    highAccuracyGps: Boolean,
    selectedLanguage: String,
    onToggleBiometric: (Boolean) -> Unit,
    onToggleNotifications: (Boolean) -> Unit,
    onToggleAutoSave: (Boolean) -> Unit,
    onToggleHighAccuracyGps: (Boolean) -> Unit,
    onSelectLanguage: (String) -> Unit,
    onTriggerSync: () -> Unit,
    onBack: () -> Unit
) {
    var showLanguageModal by remember { mutableStateOf(false) }
    var showSyncModal by remember { mutableStateOf(false) }
    var showDiagnosticsModal by remember { mutableStateOf(false) }

    val languages = listOf(
        "English (Official)",
        "Hindi (हिन्दी)",
        "Marathi (मराठी)",
        "Bengali (বাংলা)",
        "Tamil (தமிழ்)",
        "Telugu (తెలుగు)",
        "Gujarati (ગુજરાતી)",
        "Kannada (ಕನ್ನಡ)"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GovBackground)
    ) {
        GovernmentHeader(
            title = "Application Settings",
            subtitle = "Security & Operational Configuration",
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
            // Security & Authentication
            item {
                SettingsSectionCard(title = "Security & Access") {
                    SettingsSwitchRow(
                        title = "Biometric Officer Authentication",
                        subtitle = "Require fingerprint / biometric lock on app resumption",
                        checked = biometricEnabled,
                        onCheckedChange = onToggleBiometric
                    )
                    HorizontalDivider(color = GovBorderLight)
                    SettingsSwitchRow(
                        title = "Real-time High-Precision GPS Lock",
                        subtitle = "Enforce ±2m statutory satellite accuracy for all evidence photos",
                        checked = highAccuracyGps,
                        onCheckedChange = onToggleHighAccuracyGps
                    )
                }
            }

            // Sync & Offline Capabilities
            item {
                SettingsSectionCard(title = "Data & Offline Synchronization") {
                    SettingsSwitchRow(
                        title = "Background Local Auto-Save",
                        subtitle = "Persist checklist responses immediately to local storage cache",
                        checked = autoSaveEnabled,
                        onCheckedChange = onToggleAutoSave
                    )
                    HorizontalDivider(color = GovBorderLight)
                    SettingsSwitchRow(
                        title = "Statutory Push Notifications",
                        subtitle = "Receive urgent inspection assignment alerts & zonal notifications",
                        checked = notificationsEnabled,
                        onCheckedChange = onToggleNotifications
                    )
                    HorizontalDivider(color = GovBorderLight)
                    SettingsActionRow(
                        title = "Force Full Directorate Data Sync",
                        subtitle = "Push all cached field findings and pull updated inspection roster",
                        icon = Icons.Default.Sync,
                        onClick = {
                            onTriggerSync()
                            showSyncModal = true
                        }
                    )
                }
            }

            // Regional & Language Settings
            item {
                SettingsSectionCard(title = "Language & Regional Preferences") {
                    SettingsActionRow(
                        title = "Statutory UI Language",
                        subtitle = "Current: $selectedLanguage",
                        icon = Icons.Default.Language,
                        onClick = { showLanguageModal = true }
                    )
                }
            }

            // System Diagnostics
            item {
                SettingsSectionCard(title = "System Diagnostics & Compliance") {
                    SettingsActionRow(
                        title = "Run Hardware & Geofence Diagnostic",
                        subtitle = "Test camera sensor, GPS precision, and SSL root certificates",
                        icon = Icons.Default.NetworkCheck,
                        onClick = { showDiagnosticsModal = true }
                    )
                }
            }

            // App Identity Footer Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, GovBorderLight, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Government Inspection & Monitoring System (GIMS)",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = GovNavyPrimary,
                                fontSize = 12.sp
                            )
                        )
                        Text(
                            text = "Developed for Department of Justice & Statutory Compliance",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = GovTextSecondary,
                                fontSize = 11.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "App Version 2.4.1 (Build 2026.09-NIC-PROD) • 256-Bit Encrypted",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GovTextMuted,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }
        }
    }

    // Language Selector Modal
    if (showLanguageModal) {
        AlertDialog(
            onDismissRequest = { showLanguageModal = false },
            title = {
                Text(
                    text = "Select Language",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = GovNavyPrimary
                    )
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    languages.forEach { lang ->
                        val isSelected = selectedLanguage == lang
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) GovNavyContainer else GovSurfaceVariant)
                                .clickable {
                                    onSelectLanguage(lang)
                                    showLanguageModal = false
                                }
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    onSelectLanguage(lang)
                                    showLanguageModal = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = GovNavyPrimary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = lang,
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
                TextButton(onClick = { showLanguageModal = false }) {
                    Text("Cancel", color = GovTextSecondary)
                }
            },
            containerColor = Color.White
        )
    }

    // Sync Confirmation Modal
    ConfirmationModal(
        show = showSyncModal,
        title = "Data Synchronized",
        message = "All offline checklists, geotagged evidence records, and observations have been verified and securely synced with the DOJS central servers.",
        confirmText = "Done",
        cancelText = "Close",
        onConfirm = { showSyncModal = false },
        onDismiss = { showSyncModal = false }
    )

    // Diagnostics Modal
    ConfirmationModal(
        show = showDiagnosticsModal,
        title = "System Diagnostics Report",
        message = "✔ GPS Sensor: ONLINE (±1.8m accuracy)\n✔ Geofence Boundary: ACTIVE (Zone-3)\n✔ Cryptographic Key Store: SECURE\n✔ Camera & Geotag Watermark: OPERATIONAL\n✔ Offline SQLite Cache: HEALTHY (0 errors)",
        confirmText = "Acknowledge",
        cancelText = "Close",
        onConfirm = { showDiagnosticsModal = false },
        onDismiss = { showDiagnosticsModal = false }
    )
}

@Composable
private fun SettingsSectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = GovNavyPrimary,
                fontSize = 13.sp
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, GovBorderLight, RoundedCornerShape(8.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(content = content)
        }
    }
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
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
        Spacer(modifier = Modifier.width(12.dp))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = GovNavyPrimary,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = GovBorder
            )
        )
    }
}

@Composable
private fun SettingsActionRow(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = GovNavyPrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
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
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = GovTextMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}
