package com.owl.ainterview.ui.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.owl.ainterview.ui.screens.home.HomeScreen
import com.owl.ainterview.ui.screens.report.ReportScreen
import com.owl.ainterview.ui.screens.session.SessionScreen
import com.owl.ainterview.ui.screens.settings.SettingsScreen
import com.owl.ainterview.ui.screens.setup.SetupScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        // 1. Home Screen
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToSetup = {
                    navController.navigate(Screen.Setup.route)
                },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToReport = { sessionId ->
                    navController.navigate("report/$sessionId")
                }
            )
        }

        // 2. Setup Screen
        composable(Screen.Setup.route) {
            SetupScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToSession = { sessionId ->
                    navController.navigate("session/$sessionId")
                }
            )
        }

        // 3. Session Screen
        composable(
            route = "session/{sessionId}",
            arguments = listOf(navArgument("sessionId") {
                type = NavType.StringType
            })
        ) {
            SessionScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToReport = { sessionId ->
                    navController.navigate("report/$sessionId")
                }
            )
        }

        // 4. Report Screen
        composable(
            route = "report/{sessionId}",
            arguments = listOf(navArgument("sessionId") {
                type = NavType.StringType
            })
        ) {
            ReportScreen(
                onHomeClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        //5. Settings Screen
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onNavigateToLanguageSelection = { Unit }
            )
        }
    }
}