package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Report
import com.example.ui.theme.*

@Composable
fun ReportCard(
    report: Report,
    onView: () -> Unit,
    onDownload: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(GovNavyContainer)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = report.id,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = GovNavyPrimary,
                                fontSize = 11.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• ${report.inspectionId}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = GovTextMuted,
                            fontSize = 11.sp
                        )
                    )
                }

                val (badgeBg, badgeText) = when (report.status) {
                    "Approved" -> Pair(GovGreenLight, GovGreenDark)
                    "Submitted" -> Pair(Color(0xFFE0F2FE), Color(0xFF0369A1))
                    "Draft" -> Pair(GovSurfaceVariant, GovTextSecondary)
                    "Returned" -> Pair(GovRedLight, GovRedDanger)
                    else -> Pair(GovSurfaceVariant, GovTextSecondary)
                }

                Box(
                    modifier = Modifier
                        .background(badgeBg, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = report.status,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = badgeText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = report.siteName,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = GovTextPrimary,
                    fontSize = 15.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "${report.organizationName} • ${report.location}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = GovTextSecondary,
                    fontSize = 12.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Score & Metric Indicators
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(GovSurfaceVariant)
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Compliance Matrix: ",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = GovTextSecondary,
                            fontSize = 11.sp
                        )
                    )
                    Text(
                        text = "${report.compliantCount} Compliant",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = GovGreenDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )
                    if (report.partialCount > 0) {
                        Text(
                            text = " • ${report.partialCount} Partial",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = GovAmberWarning,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        )
                    }
                    if (report.nonCompliantCount > 0) {
                        Text(
                            text = " • ${report.nonCompliantCount} Non-Comp",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = GovRedDanger,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.EventAvailable,
                    contentDescription = null,
                    tint = GovTextMuted,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Filed: ${report.submissionDate} by ${report.inspectorName}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = GovTextMuted,
                        fontSize = 11.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons (View, Download, Share)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onView,
                    modifier = Modifier
                        .weight(1.2f)
                        .height(36.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GovNavyPrimary),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "View Report",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    )
                }

                OutlinedButton(
                    onClick = onDownload,
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    shape = RoundedCornerShape(4.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(GovBorder)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = GovNavyPrimary),
                    contentPadding = PaddingValues(horizontal = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "PDF",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    )
                }

                IconButton(
                    onClick = onShare,
                    modifier = Modifier
                        .size(36.dp)
                        .border(1.dp, GovBorder, RoundedCornerShape(4.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = GovNavyPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
