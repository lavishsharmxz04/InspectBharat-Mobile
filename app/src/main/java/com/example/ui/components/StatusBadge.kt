package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*

@Composable
fun StatusBadge(
    status: InspectionStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, borderColor) = when (status) {
        InspectionStatus.PENDING -> Triple(GovAmberLight, GovAmberWarning, GovAmberWarning.copy(alpha = 0.4f))
        InspectionStatus.IN_PROGRESS -> Triple(GovNavyContainer, GovNavyPrimary, GovNavyPrimary.copy(alpha = 0.4f))
        InspectionStatus.COMPLETED -> Triple(GovGreenLight, GovGreenDark, GovIndiaGreen.copy(alpha = 0.4f))
        InspectionStatus.SUBMITTED -> Triple(Color(0xFFE0F2FE), Color(0xFF0369A1), Color(0xFF0284C7).copy(alpha = 0.4f))
    }

    Box(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(4.dp))
            .border(1.dp, borderColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = status.label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
        )
    }
}

@Composable
fun PriorityBadge(
    priority: Priority,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (priority) {
        Priority.HIGH -> Pair(GovRedLight, GovRedDanger)
        Priority.MEDIUM -> Pair(GovAmberLight, GovAmberWarning)
        Priority.LOW -> Pair(GovSurfaceVariant, GovTextSecondary)
    }

    Box(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = priority.label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = textColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 10.sp
            )
        )
    }
}

@Composable
fun ComplianceBadge(
    status: ComplianceStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (status) {
        ComplianceStatus.COMPLIANT -> Triple(GovGreenLight, GovGreenDark, "Compliant")
        ComplianceStatus.PARTIALLY_COMPLIANT -> Triple(GovAmberLight, GovAmberWarning, "Partially Compliant")
        ComplianceStatus.NON_COMPLIANT -> Triple(GovRedLight, GovRedDanger, "Non-Compliant")
        ComplianceStatus.NOT_APPLICABLE -> Triple(GovSurfaceVariant, GovTextMuted, "N/A")
        ComplianceStatus.UNANSWERED -> Triple(Color(0xFFF1F5F9), Color(0xFF64748B), "Pending")
    }

    Box(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
        )
    }
}

@Composable
fun SeverityBadge(
    severity: Severity,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (severity) {
        Severity.CRITICAL -> Pair(GovRedLight, GovRedDanger)
        Severity.HIGH -> Pair(Color(0xFFFFEDD5), Color(0xFFC2410C))
        Severity.MEDIUM -> Pair(GovAmberLight, GovAmberWarning)
        Severity.LOW -> Pair(GovSurfaceVariant, GovTextSecondary)
    }

    Box(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = "${severity.label} Severity",
            style = MaterialTheme.typography.labelSmall.copy(
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
        )
    }
}
