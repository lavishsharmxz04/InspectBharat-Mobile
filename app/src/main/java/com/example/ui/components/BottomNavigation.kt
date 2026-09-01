package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

enum class BottomTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME("Home", Icons.Default.Dashboard, Icons.Outlined.Dashboard),
    INSPECTIONS("Inspections", Icons.AutoMirrored.Filled.Assignment, Icons.AutoMirrored.Outlined.Assignment),
    REPORTS("Reports", Icons.Default.Assessment, Icons.Outlined.Assessment),
    NOTIFICATIONS("Alerts", Icons.Default.Notifications, Icons.Outlined.Notifications),
    PROFILE("Profile", Icons.Default.AccountCircle, Icons.Outlined.AccountCircle)
}

@Composable
fun AppBottomNavigation(
    selectedTab: BottomTab,
    onTabSelected: (BottomTab) -> Unit,
    unreadNotifications: Int = 0,
    pendingInspections: Int = 0
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp),
        color = Color.White,
        tonalElevation = 6.dp
    ) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            containerColor = Color.White,
            contentColor = GovNavyPrimary,
            tonalElevation = 0.dp
        ) {
            BottomTab.values().forEach { tab ->
                val isSelected = tab == selectedTab

                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onTabSelected(tab) },
                    icon = {
                        Box {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.title,
                                modifier = Modifier.size(24.dp),
                                tint = if (isSelected) GovNavyPrimary else GovTextMuted
                            )
                            if (tab == BottomTab.NOTIFICATIONS && unreadNotifications > 0) {
                                Badge(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .offset(x = 6.dp, y = (-4).dp),
                                    containerColor = GovRedDanger,
                                    contentColor = Color.White
                                ) {
                                    Text(
                                        text = if (unreadNotifications > 9) "9+" else unreadNotifications.toString(),
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp)
                                    )
                                }
                            }
                            if (tab == BottomTab.INSPECTIONS && pendingInspections > 0 && !isSelected) {
                                Badge(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .offset(x = 6.dp, y = (-4).dp),
                                    containerColor = GovAmberWarning,
                                    contentColor = Color.White
                                ) {
                                    Text(
                                        text = pendingInspections.toString(),
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp)
                                    )
                                }
                            }
                        }
                    },
                    label = {
                        Text(
                            text = tab.title,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 11.sp,
                                color = if (isSelected) GovNavyPrimary else GovTextMuted
                            )
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = GovNavyContainer,
                        selectedIconColor = GovNavyPrimary,
                        unselectedIconColor = GovTextMuted
                    )
                )
            }
        }
    }
}
