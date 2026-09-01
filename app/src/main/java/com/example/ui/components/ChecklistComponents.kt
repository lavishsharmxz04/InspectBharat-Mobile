package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.data.model.ChecklistItem
import com.example.data.model.ComplianceStatus
import com.example.ui.theme.*

@Composable
fun ChecklistItemCard(
    item: ChecklistItem,
    index: Int,
    onStatusChange: (ComplianceStatus, String) -> Unit,
    onAddEvidence: () -> Unit,
    attachedEvidenceCount: Int = 0,
    modifier: Modifier = Modifier
) {
    var remarks by remember(item.id, item.remarks) { mutableStateOf(item.remarks) }
    var showRemarksField by remember(item.id) { mutableStateOf(item.remarks.isNotEmpty() || item.status == ComplianceStatus.NON_COMPLIANT || item.status == ComplianceStatus.PARTIALLY_COMPLIANT) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(
                1.dp,
                if (item.status == ComplianceStatus.NON_COMPLIANT) GovRedDanger.copy(alpha = 0.5f)
                else GovBorderLight,
                RoundedCornerShape(8.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (item.status == ComplianceStatus.NON_COMPLIANT) Color(0xFFFFF7F7) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header with Item ID and Section & Evidence Required Indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(GovNavyPrimary)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "#$index (${item.id})",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 10.sp
                            )
                        )
                    }

                    if (item.isEvidenceRequired) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(GovSaffronLight)
                                .border(1.dp, GovSaffronAccent.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Photo Mandate",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GovAmberWarning,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }

                ComplianceBadge(status = item.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Question Text
            Text(
                text = item.question,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = GovTextPrimary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            )

            // Statutory Reference
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Ref: ${item.statutoryReference}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = GovTextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Options Selection Buttons Grid/Row
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ComplianceOptionButton(
                        text = "Compliant",
                        icon = Icons.Default.CheckCircle,
                        isSelected = item.status == ComplianceStatus.COMPLIANT,
                        activeBgColor = GovGreenLight,
                        activeTextColor = GovGreenDark,
                        activeBorderColor = GovIndiaGreen,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onStatusChange(ComplianceStatus.COMPLIANT, remarks)
                        }
                    )

                    ComplianceOptionButton(
                        text = "Partial",
                        icon = Icons.Default.WarningAmber,
                        isSelected = item.status == ComplianceStatus.PARTIALLY_COMPLIANT,
                        activeBgColor = GovAmberLight,
                        activeTextColor = GovAmberWarning,
                        activeBorderColor = GovAmberWarning,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            showRemarksField = true
                            onStatusChange(ComplianceStatus.PARTIALLY_COMPLIANT, remarks)
                        }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ComplianceOptionButton(
                        text = "Non-Compliant",
                        icon = Icons.Default.Cancel,
                        isSelected = item.status == ComplianceStatus.NON_COMPLIANT,
                        activeBgColor = GovRedLight,
                        activeTextColor = GovRedDanger,
                        activeBorderColor = GovRedDanger,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            showRemarksField = true
                            onStatusChange(ComplianceStatus.NON_COMPLIANT, remarks)
                        }
                    )

                    ComplianceOptionButton(
                        text = "N/A",
                        icon = Icons.Default.RemoveCircleOutline,
                        isSelected = item.status == ComplianceStatus.NOT_APPLICABLE,
                        activeBgColor = GovSurfaceVariant,
                        activeTextColor = GovTextSecondary,
                        activeBorderColor = GovBorder,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onStatusChange(ComplianceStatus.NOT_APPLICABLE, remarks)
                        }
                    )
                }
            }

            // Remarks Field
            Spacer(modifier = Modifier.height(10.dp))
            if (showRemarksField || remarks.isNotEmpty()) {
                OutlinedTextField(
                    value = remarks,
                    onValueChange = {
                        remarks = it
                        onStatusChange(item.status, it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Inspector Observations / Remarks", fontSize = 11.sp) },
                    placeholder = { Text("Add specific compliance findings...", fontSize = 12.sp) },
                    minLines = 2,
                    maxLines = 4,
                    textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                    shape = RoundedCornerShape(6.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GovNavyPrimary,
                        unfocusedBorderColor = GovBorderLight,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
            } else {
                TextButton(
                    onClick = { showRemarksField = true },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddComment,
                        contentDescription = null,
                        tint = GovNavyPrimary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Add Specific Remark",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = GovNavyPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            // Evidence Action & Count
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (attachedEvidenceCount > 0) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = null,
                            tint = GovIndiaGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$attachedEvidenceCount Evidence Attached",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = GovIndiaGreen,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp
                            )
                        )
                    }
                } else {
                    Text(
                        text = if (item.isEvidenceRequired) "• Photographic evidence required" else "• Optional photo proof",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (item.isEvidenceRequired) GovAmberWarning else GovTextMuted,
                            fontSize = 11.sp
                        )
                    )
                }

                OutlinedButton(
                    onClick = onAddEvidence,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.height(32.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GovNavyPrimary),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(GovNavyPrimary)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Add Evidence",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ComplianceOptionButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    activeBgColor: Color,
    activeTextColor: Color,
    activeBorderColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(38.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(if (isSelected) activeBgColor else Color.White)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) activeBorderColor else GovBorderLight,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) activeTextColor else GovTextMuted,
                modifier = Modifier.size(15.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) activeTextColor else GovTextSecondary,
                    fontSize = 11.sp
                ),
                maxLines = 1
            )
        }
    }
}
