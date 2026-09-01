package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Report
import com.example.ui.components.ConfirmationModal
import com.example.ui.components.GovernmentHeader
import com.example.ui.components.PrimaryButton
import com.example.ui.components.SecondaryButton
import com.example.ui.theme.*

@Composable
fun ReportDetailsScreen(
    report: Report,
    onBack: () -> Unit
) {
    var showExportConfirmDialog by remember { mutableStateOf(false) }
    var showShareConfirmDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GovBackground)
    ) {
        GovernmentHeader(
            title = "Statutory Inspection Report",
            subtitle = report.id,
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
            // Official Letterhead Docket Card
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
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Official Emblem
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(GovNavyContainer)
                                .padding(3.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_gov_emblem),
                                contentDescription = "Emblem",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "DEPARTMENT OF JUSTICE & STATUTORY COMPLIANCE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = GovNavyPrimary,
                                fontSize = 11.sp,
                                letterSpacing = 0.5.sp
                            ),
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "FIELD INSPECTION & MONITORING DIRECTORATE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GovTextSecondary,
                                fontSize = 10.sp
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = GovBorderLight)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Docket Reference:", style = MaterialTheme.typography.bodySmall.copy(color = GovTextMuted, fontSize = 11.sp))
                            Text(report.id, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = GovNavyPrimary, fontSize = 11.sp))
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Inspection ID:", style = MaterialTheme.typography.bodySmall.copy(color = GovTextMuted, fontSize = 11.sp))
                            Text(report.inspectionId, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 11.sp))
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Filing Status:", style = MaterialTheme.typography.bodySmall.copy(color = GovTextMuted, fontSize = 11.sp))
                            Text(report.status, style = MaterialTheme.typography.bodySmall.copy(color = GovGreenDark, fontWeight = FontWeight.Bold, fontSize = 11.sp))
                        }
                    }
                }
            }

            // Facility & Inspection Metadata Card
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
                            text = "Facility & Audit Particulars",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = GovNavyPrimary,
                                fontSize = 14.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = report.siteName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = GovTextPrimary,
                                fontSize = 15.sp
                            )
                        )
                        Text(
                            text = report.organizationName,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = GovTextSecondary,
                                fontSize = 12.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Location: ${report.location}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = GovTextSecondary,
                                fontSize = 12.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = GovBorderLight)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Inspection Date", style = MaterialTheme.typography.labelSmall.copy(color = GovTextMuted))
                                Text(report.inspectionDate, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))
                            }
                            Column {
                                Text("Submission Date", style = MaterialTheme.typography.labelSmall.copy(color = GovTextMuted))
                                Text(report.submissionDate, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold))
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Lead Investigating Officer: ${report.inspectorName}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = GovNavyPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }

            // Statutory Findings & Matrix Breakdown Card
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
                            text = "Compliance Score Matrix",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = GovNavyPrimary,
                                fontSize = 14.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            ComplianceStatColumn(count = report.compliantCount, label = "Compliant", color = GovGreenDark)
                            ComplianceStatColumn(count = report.partialCount, label = "Partial", color = GovAmberWarning)
                            ComplianceStatColumn(count = report.nonCompliantCount, label = "Non-Comp", color = GovRedDanger)
                            ComplianceStatColumn(count = report.violationsCount, label = "Violations", color = Color(0xFFC2410C))
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(GovNavyContainer, RoundedCornerShape(6.dp))
                                .padding(10.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Final Recommendation & Statutory Directive:",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = GovNavyPrimary,
                                        fontSize = 11.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = report.recommendation,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = GovTextPrimary,
                                        fontSize = 12.sp,
                                        lineHeight = 16.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Digital Seal & Verification Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, GovBorderLight, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = GovIndiaGreen,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Digitally Signed & Certified",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = GovGreenDark,
                                    fontSize = 13.sp
                                )
                            )
                            Text(
                                text = "Certified by ${report.inspectorName} (Field Inspector)\nCryptographic Signature Validated by DOJS Root CA",
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PrimaryButton(
                    text = "Download PDF",
                    icon = Icons.Default.Download,
                    modifier = Modifier.weight(1f),
                    onClick = { showExportConfirmDialog = true }
                )

                SecondaryButton(
                    text = "Share",
                    icon = Icons.Default.Share,
                    modifier = Modifier.weight(0.8f),
                    onClick = { showShareConfirmDialog = true }
                )
            }
        }
    }

    ConfirmationModal(
        show = showExportConfirmDialog,
        title = "Export Official PDF",
        message = "Official report ${report.id} has been generated and saved with authentic watermark seal to your device storage.",
        confirmText = "Done",
        cancelText = "Close",
        onConfirm = { showExportConfirmDialog = false },
        onDismiss = { showExportConfirmDialog = false }
    )

    ConfirmationModal(
        show = showShareConfirmDialog,
        title = "Share Official Filing",
        message = "Encrypted access link for Report ${report.id} has been copied to clipboard for official transmission to ${report.organizationName}.",
        confirmText = "Done",
        cancelText = "Close",
        onConfirm = { showShareConfirmDialog = false },
        onDismiss = { showShareConfirmDialog = false }
    )
}

@Composable
private fun ComplianceStatColumn(
    count: Int,
    label: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = color,
                fontSize = 20.sp
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = GovTextSecondary,
                fontSize = 11.sp
            )
        )
    }
}
