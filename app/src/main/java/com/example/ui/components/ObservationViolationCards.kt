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
import com.example.data.model.Observation
import com.example.data.model.Severity
import com.example.data.model.Violation
import com.example.ui.theme.*

@Composable
fun ObservationCard(
    observation: Observation,
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
                            text = observation.id,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = GovNavyPrimary,
                                fontSize = 10.sp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(GovSurfaceVariant)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = observation.category,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GovTextSecondary,
                                fontWeight = FontWeight.Medium,
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                SeverityBadge(severity = observation.severity)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = observation.title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = GovTextPrimary,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = observation.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = GovTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Logged: ${observation.timestamp}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = GovTextMuted,
                        fontSize = 10.sp
                    )
                )

                if (observation.linkedEvidenceIds.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AttachFile,
                            contentDescription = null,
                            tint = GovNavyPrimary,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${observation.linkedEvidenceIds.size} Evidence Linked",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GovNavyPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ViolationCard(
    violation: Violation,
    modifier: Modifier = Modifier
) {
    val isCritical = violation.severity == Severity.CRITICAL || violation.severity == Severity.HIGH

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .border(
                1.5.dp,
                if (isCritical) GovRedDanger else GovAmberWarning,
                RoundedCornerShape(8.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isCritical) Color(0xFFFFF5F5) else Color(0xFFFFFBEB)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header with Alert banner
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isCritical) Icons.Default.ReportProblem else Icons.Default.Warning,
                        contentDescription = null,
                        tint = if (isCritical) GovRedDanger else GovAmberWarning,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "STATUTORY VIOLATION (${violation.id})",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isCritical) GovRedDanger else GovAmberWarning,
                            fontSize = 11.sp
                        )
                    )
                }

                SeverityBadge(severity = violation.severity)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = violation.violationType,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = GovTextPrimary,
                    fontSize = 14.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = violation.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = GovTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Recommended Action & Deadline box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White)
                    .border(1.dp, GovBorderLight, RoundedCornerShape(6.dp))
                    .padding(10.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Mandated Corrective Action: ",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = GovNavyPrimary,
                                fontSize = 11.sp
                            )
                        )
                    }
                    Text(
                        text = violation.recommendedAction,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = GovTextPrimary,
                            fontSize = 12.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Statutory Rectification Deadline:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = GovTextMuted,
                                fontSize = 10.sp
                            )
                        )
                        Box(
                            modifier = Modifier
                                .background(GovRedLight, RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = violation.correctiveDeadline,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = GovRedDanger,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
