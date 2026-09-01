package com.example.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.model.InspectionStatus
import com.example.data.repository.InspectionRepository
import com.example.ui.components.AppBottomNavigation
import com.example.ui.components.BottomTab
import com.example.ui.screens.*

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val MAIN = "main"
    const val INSPECTION_DETAILS = "inspection_details/{inspectionId}"
    const val ACTIVE_INSPECTION = "active_inspection/{inspectionId}"
    const val SUBMISSION_SUCCESS = "submission_success/{ackId}"
    const val REPORT_DETAILS = "report_details/{reportId}"
    const val TEAM = "team"
    const val SETTINGS = "settings"

    fun inspectionDetails(id: String) = "inspection_details/$id"
    fun activeInspection(id: String) = "active_inspection/$id"
    fun submissionSuccess(ackId: String) = "submission_success/$ackId"
    fun reportDetails(id: String) = "report_details/$id"
}

@Composable
fun AppNavigation(
    repository: InspectionRepository,
    navController: NavHostController = rememberNavController()
) {
    val isLoggedIn by repository.isLoggedIn.collectAsState()
    val currentUser by repository.currentUser.collectAsState()
    val inspections by repository.inspections.collectAsState()
    val reports by repository.reports.collectAsState()
    val notifications by repository.notifications.collectAsState()
    val teamMembers by repository.teamMembers.collectAsState()
    val isOffline by repository.isOfflineMode.collectAsState()
    val lastSyncTime by repository.lastSyncTime.collectAsState()

    val biometricEnabled by repository.biometricEnabled.collectAsState()
    val notificationsEnabled by repository.notificationsEnabled.collectAsState()
    val autoSaveEnabled by repository.autoSaveEnabled.collectAsState()
    val highAccuracyGps by repository.highAccuracyGps.collectAsState()
    val selectedLanguage by repository.selectedLanguage.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(if (isLoggedIn) Routes.MAIN else Routes.LOGIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { user, pass ->
                    val success = repository.login(user, pass)
                    if (success) {
                        navController.navigate(Routes.MAIN) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                    success
                }
            )
        }

        composable(Routes.MAIN) {
            var selectedBottomTab by remember { mutableStateOf(BottomTab.HOME) }
            var inspectionStatusFilter by remember { mutableStateOf<InspectionStatus?>(null) }

            val unreadCount = notifications.count { !it.isRead }
            val pendingCount = inspections.count { it.status == InspectionStatus.PENDING }

            Scaffold(
                bottomBar = {
                    AppBottomNavigation(
                        selectedTab = selectedBottomTab,
                        onTabSelected = { tab ->
                            if (tab == BottomTab.INSPECTIONS) {
                                inspectionStatusFilter = null
                            }
                            selectedBottomTab = tab
                        },
                        unreadNotifications = unreadCount,
                        pendingInspections = pendingCount
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (selectedBottomTab) {
                        BottomTab.HOME -> {
                            DashboardScreen(
                                user = currentUser,
                                inspections = inspections,
                                unreadNotificationCount = unreadCount,
                                isOffline = isOffline,
                                lastSyncTime = lastSyncTime,
                                onToggleOffline = { repository.toggleOfflineMode() },
                                onSyncClick = { repository.triggerSync() },
                                onNotificationClick = { selectedBottomTab = BottomTab.NOTIFICATIONS },
                                onProfileClick = { selectedBottomTab = BottomTab.PROFILE },
                                onViewAllInspections = { filter ->
                                    inspectionStatusFilter = filter
                                    selectedBottomTab = BottomTab.INSPECTIONS
                                },
                                onViewReports = { selectedBottomTab = BottomTab.REPORTS },
                                onSelectInspection = { id ->
                                    navController.navigate(Routes.inspectionDetails(id))
                                },
                                onStartInspection = { id ->
                                    repository.startInspection(id)
                                    navController.navigate(Routes.activeInspection(id))
                                }
                            )
                        }

                        BottomTab.INSPECTIONS -> {
                            InspectionsScreen(
                                inspections = inspections,
                                initialStatusFilter = inspectionStatusFilter,
                                onSelectInspection = { id ->
                                    navController.navigate(Routes.inspectionDetails(id))
                                },
                                onStartInspection = { id ->
                                    val insp = inspections.find { it.id == id }
                                    if (insp?.status == InspectionStatus.COMPLETED || insp?.status == InspectionStatus.SUBMITTED) {
                                        navController.navigate(Routes.inspectionDetails(id))
                                    } else {
                                        repository.startInspection(id)
                                        navController.navigate(Routes.activeInspection(id))
                                    }
                                },
                                onBack = { selectedBottomTab = BottomTab.HOME }
                            )
                        }

                        BottomTab.REPORTS -> {
                            ReportsScreen(
                                reports = reports,
                                onSelectReport = { reportId ->
                                    navController.navigate(Routes.reportDetails(reportId))
                                },
                                onBack = { selectedBottomTab = BottomTab.HOME }
                            )
                        }

                        BottomTab.NOTIFICATIONS -> {
                            NotificationsScreen(
                                notifications = notifications,
                                onMarkRead = { repository.markNotificationRead(it) },
                                onMarkAllRead = { repository.markAllNotificationsRead() },
                                onSelectInspection = { id ->
                                    navController.navigate(Routes.inspectionDetails(id))
                                },
                                onBack = { selectedBottomTab = BottomTab.HOME }
                            )
                        }

                        BottomTab.PROFILE -> {
                            ProfileScreen(
                                user = currentUser,
                                onNavigateTeam = { navController.navigate(Routes.TEAM) },
                                onNavigateSettings = { navController.navigate(Routes.SETTINGS) },
                                onLogout = {
                                    repository.logout()
                                    navController.navigate(Routes.LOGIN) {
                                        popUpTo(Routes.MAIN) { inclusive = true }
                                    }
                                },
                                onBack = { selectedBottomTab = BottomTab.HOME }
                            )
                        }
                    }
                }
            }
        }

        composable(
            route = Routes.INSPECTION_DETAILS,
            arguments = listOf(navArgument("inspectionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val inspectionId = backStackEntry.arguments?.getString("inspectionId") ?: ""
            val inspection = inspections.find { it.id == inspectionId } ?: inspections.first()

            InspectionDetailsScreen(
                inspection = inspection,
                onStartOrResumeInspection = { id ->
                    repository.startInspection(id)
                    navController.navigate(Routes.activeInspection(id))
                },
                onViewReport = { id ->
                    val matchingReport = reports.find { it.inspectionId == id } ?: reports.first()
                    navController.navigate(Routes.reportDetails(matchingReport.id))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.ACTIVE_INSPECTION,
            arguments = listOf(navArgument("inspectionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val inspectionId = backStackEntry.arguments?.getString("inspectionId") ?: ""
            val inspection = inspections.find { it.id == inspectionId } ?: inspections.first()

            ActiveInspectionScreen(
                inspection = inspection,
                currentUser = currentUser,
                onUpdateChecklist = { itemId, status, remarks ->
                    repository.updateChecklistItem(inspection.id, itemId, status, remarks)
                },
                onAddEvidence = { chkId, title, type, desc ->
                    repository.addEvidence(inspection.id, chkId, title, type, desc)
                },
                onDeleteEvidence = { evId ->
                    repository.deleteEvidence(inspection.id, evId)
                },
                onAddObservation = { title, cat, sev, desc ->
                    repository.addObservation(inspection.id, title, cat, sev, desc)
                },
                onAddViolation = { type, sev, desc, item, act, dead ->
                    repository.addViolation(inspection.id, type, sev, desc, item, act, dead)
                },
                onSubmitInspection = { remarks, rec, sig ->
                    repository.submitInspection(inspection.id, remarks, rec, sig)
                },
                onBack = { navController.popBackStack() },
                onSubmitSuccess = { ackId ->
                    navController.navigate(Routes.submissionSuccess(ackId)) {
                        popUpTo(Routes.activeInspection(inspection.id)) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.SUBMISSION_SUCCESS,
            arguments = listOf(navArgument("ackId") { type = NavType.StringType })
        ) { backStackEntry ->
            val ackId = backStackEntry.arguments?.getString("ackId") ?: "ACK-DOJS-000000"

            SubmissionSuccessScreen(
                ackId = ackId,
                onViewReport = {
                    val latestReport = reports.firstOrNull() ?: reports.first()
                    navController.navigate(Routes.reportDetails(latestReport.id)) {
                        popUpTo(Routes.MAIN)
                    }
                },
                onReturnToDashboard = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.MAIN) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.REPORT_DETAILS,
            arguments = listOf(navArgument("reportId") { type = NavType.StringType })
        ) { backStackEntry ->
            val reportId = backStackEntry.arguments?.getString("reportId") ?: ""
            val report = reports.find { it.id == reportId } ?: reports.first()

            ReportDetailsScreen(
                report = report,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.TEAM) {
            TeamScreen(
                teamMembers = teamMembers,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                biometricEnabled = biometricEnabled,
                notificationsEnabled = notificationsEnabled,
                autoSaveEnabled = autoSaveEnabled,
                highAccuracyGps = highAccuracyGps,
                selectedLanguage = selectedLanguage,
                onToggleBiometric = { repository.toggleBiometric(it) },
                onToggleNotifications = { repository.toggleNotifications(it) },
                onToggleAutoSave = { repository.toggleAutoSave(it) },
                onToggleHighAccuracyGps = { repository.toggleHighAccuracyGps(it) },
                onSelectLanguage = { repository.setLanguage(it) },
                onTriggerSync = { repository.triggerSync() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
