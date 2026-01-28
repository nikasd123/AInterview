package com.owl.ainterview

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.owl.ainterview.core.LocaleUtils
import com.owl.ainterview.ui.nav.AppNavigation
import com.owl.ainterview.ui.nav.Screen
import com.owl.ainterview.ui.screens.home.HomeScreen
import com.owl.ainterview.ui.screens.report.ReportScreen
import com.owl.ainterview.ui.screens.session.SessionScreen
import com.owl.ainterview.ui.screens.settings.SettingsScreen
import com.owl.ainterview.ui.screens.setup.SetupScreen
import com.owl.ainterview.ui.theme.AIInterviewerTheme
import com.owl.domain.model.AppLanguage
import com.owl.domain.port.repository.AppSettingsRepository
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.compose.viewmodel.koinActivityViewModel
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AIInterviewerTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}