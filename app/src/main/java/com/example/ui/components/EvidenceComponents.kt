package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.model.Evidence
import com.example.ui.theme.*

@Composable
fun EvidenceCard(
    evidence: Evidence,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDetailDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, GovBorderLight, RoundedCornerShape(8.dp))
            .clickable { showDetailDialog = true },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Simulated Geotagged Photo Thumbnail
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        Brush.linearGradient(listOf(GovNavyPrimary, GovNavyDark))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = when (evidence.type) {
                            "DOCUMENT" -> Icons.Default.Description
                            "NOTE" -> Icons.Default.NoteAlt
                            else -> Icons.Default.CameraAlt
                        },
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "GEOTAG",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFFFDE68A),
                            fontWeight = FontWeight.Bold,
                            fontSize = 8.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(GovNavyContainer, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = evidence.id,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = GovNavyPrimary,
                                fontSize = 10.sp
                            )
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Delete Evidence",
                            tint = GovRedDanger,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = evidence.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = GovTextPrimary,
                        fontSize = 13.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = evidence.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = GovTextSecondary,
                        fontSize = 11.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationSearching,
                        contentDescription = null,
                        tint = GovTextMuted,
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${evidence.coordinates} • ${evidence.timestamp}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = GovTextMuted,
                            fontSize = 10.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }

    if (showDetailDialog) {
        Dialog(onDismissRequest = { showDetailDialog = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                color = Color.White
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
                        Text(
                            text = "Geotagged Field Evidence",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = GovNavyPrimary
                            )
                        )
                        IconButton(
                            onClick = { showDetailDialog = false },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = GovTextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Simulated High-Res Field Frame with Timestamp and Geotag Watermark
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.verticalGradient(listOf(GovNavyDark, Color(0xFF0F172A)))
                            )
                            .border(1.dp, GovBorder, RoundedCornerShape(8.dp))
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
                                    text = "OFFICIAL DOJS RECORD",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFFFF9933),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                                Text(
                                    text = evidence.id,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }

                            Box(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.3f),
                                    modifier = Modifier.size(60.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = "LAT/LONG: ${evidence.coordinates}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF38BDF8),
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 10.sp
                                    )
                                )
                                Text(
                                    text = "LOC: ${evidence.location}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White.copy(alpha = 0.9f),
                                        fontSize = 9.sp
                                    )
                                )
                                Text(
                                    text = "TIME: ${evidence.timestamp}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White.copy(alpha = 0.9f),
                                        fontSize = 9.sp
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = evidence.title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = GovTextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = evidence.description,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = GovTextSecondary,
                            fontSize = 13.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PrimaryButton(
                        text = "Close Preview",
                        onClick = { showDetailDialog = false }
                    )
                }
            }
        }
    }
}

@Composable
fun CaptureEvidenceModal(
    show: Boolean,
    onDismiss: () -> Unit,
    onEvidenceCaptured: (title: String, type: String, description: String) -> Unit
) {
    if (!show) return

    var selectedMode by remember { mutableStateOf("PHOTO") } // PHOTO, GALLERY, DOC, NOTE
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedPreset by remember { mutableStateOf("Fire Safety Apparatus") }

    val presetOptions = listOf(
        "Fire Safety Apparatus",
        "Emergency Exit Obstruction",
        "Electrical Earthing Panel",
        "Hazardous Chemical Bunding",
        "Worker PPE Verification",
        "Statutory Display Board",
        "Effluent Treatment Readout"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Modal Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Capture Field Evidence",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = GovNavyPrimary,
                                fontSize = 16.sp
                            )
                        )
                        Text(
                            text = "Geotagged & Timestamped Official Proof",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = GovTextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = GovTextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Mode Tabs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        Triple("PHOTO", "Camera", Icons.Default.CameraAlt),
                        Triple("GALLERY", "Gallery", Icons.Default.PhotoLibrary),
                        Triple("DOC", "Document", Icons.Default.AttachFile),
                        Triple("NOTE", "Field Note", Icons.Default.EditNote)
                    ).forEach { (modeKey, label, icon) ->
                        val isSelected = selectedMode == modeKey
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) GovNavyPrimary else GovSurfaceVariant)
                                .clickable { selectedMode = modeKey },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (isSelected) Color.White else GovNavyPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isSelected) Color.White else GovTextPrimary,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Simulated Viewfinder Frame for Camera Mode
                if (selectedMode == "PHOTO") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0F172A))
                            .border(1.5.dp, Color(0xFF38BDF8), RoundedCornerShape(8.dp))
                            .padding(8.dp)
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
                                    text = "● LIVE GPS LOCK",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF22C55E),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp
                                    )
                                )
                                Text(
                                    text = "ACCURACY: ±1.8m",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White,
                                        fontSize = 9.sp
                                    )
                                )
                            }

                            Box(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CropFree,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.5f),
                                    modifier = Modifier.size(40.dp)
                                )
                            }

                            Text(
                                text = "28.5355° N, 77.2690° E | Okhla Industrial Area Ph-III",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFFFDE68A),
                                    fontSize = 9.sp
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Preset selector chips
                Text(
                    text = "Quick Subject Category:",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = GovTextSecondary,
                        fontSize = 11.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    presetOptions.take(3).forEach { preset ->
                        val isSelected = selectedPreset == preset
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isSelected) GovNavyContainer else GovSurfaceVariant)
                                .border(1.dp, if (isSelected) GovNavyPrimary else GovBorderLight, RoundedCornerShape(4.dp))
                                .clickable {
                                    selectedPreset = preset
                                    if (title.isEmpty()) title = preset
                                }
                                .padding(horizontal = 6.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = preset,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isSelected) GovNavyPrimary else GovTextSecondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 9.sp
                                ),
                                maxLines = 1
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                InputField(
                    value = title,
                    onValueChange = { title = it },
                    label = "Evidence Title / Label",
                    placeholder = "e.g. $selectedPreset Verification"
                )

                Spacer(modifier = Modifier.height(8.dp))

                InputField(
                    value = description,
                    onValueChange = { description = it },
                    label = "Field Observations / Details",
                    placeholder = "Enter physical condition, serial number or exact spot...",
                    singleLine = false,
                    minLines = 2,
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                PrimaryButton(
                    text = "Attach & Record Evidence",
                    icon = Icons.Default.Check,
                    onClick = {
                        val finalTitle = if (title.isNotBlank()) title else "$selectedPreset Proof"
                        val finalDesc = if (description.isNotBlank()) description else "Verified and captured during official field audit."
                        onEvidenceCaptured(finalTitle, selectedMode, finalDesc)
                        onDismiss()
                    }
                )
            }
        }
    }
}
