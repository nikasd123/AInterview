package com.owl.ainterview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.owl.ainterview.ui.nav.Screen
import com.owl.ainterview.ui.screens.session.SessionScreen
import com.owl.ainterview.ui.screens.setup.SetupScreen
import com.owl.ainterview.ui.theme.AIInterviewerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AIInterviewerTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Setup.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // 1. Home Screen
                        composable(Screen.Home.route) {
                            // Пока заглушки, чтобы проверить навигацию
                            PlaceholderScreen("Home Screen", onClick = { navController.navigate(Screen.Setup.route) })
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
                            route = "session/{sessionId}", // Указываем аргумент в маршруте
                            arguments = listOf(navArgument("sessionId") { type = NavType.StringType })
                        ) {
                            SessionScreen(
                                onBackClick = { navController.popBackStack() },
                                onNavigateToReport = {
                                    // Пока заглушка. Позже сделаем переход на ReportScreen
                                    navController.popBackStack(Screen.Home.route, false)
                                }
                            )
                        }

                        // 4. Report Screen
                        composable(Screen.Report.route) {
                            PlaceholderScreen("Report Screen", onClick = { navController.popBackStack(Screen.Home.route, false) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceholderScreen(title: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("Current: $title. Go Next ->")
    }
}