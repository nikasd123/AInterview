package com.owl.ainterview.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.owl.ainterview.ui.screens.settings.components.SettingsCard
import com.owl.ainterview.ui.screens.settings.components.SettingsIcon
import com.owl.ainterview.ui.screens.settings.components.SettingsItem
import com.owl.ainterview.ui.screens.settings.components.SettingsSection
import com.owl.ainterview.ui.screens.settings.components.SettingsSwitchItem
import com.owl.ainterview.ui.theme.DarkBackground
import com.owl.ainterview.ui.theme.NeonGreen
import com.owl.ainterview.ui.theme.TextSecondary
import com.owl.domain.model.AppLanguage
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onNavigateToLanguageSelection: () -> Unit
) {
    val viewModel = koinViewModel<SettingsViewModel>()
    val currentLanguage by viewModel.currentLanguage.collectAsState()

    val state = SettingsState(
        currentLanguage = currentLanguage,
        isRemindersEnabled = true,
        isTechTipsEnabled = false,
        speechSpeed = 1.0f
    )

    SettingsScreenContent(
        state = state,
        onBackClick = onBackClick,
        onLanguageClick = {
            val nextLang =
                if (state.currentLanguage == AppLanguage.ENGLISH) AppLanguage.RUSSIAN
                else AppLanguage.ENGLISH
            viewModel.onLanguageSelected(nextLang)
        },
        onReminderToggle = { /* viewModel.toggleReminders(it) */ },
        onTipsToggle = { /* viewModel.toggleTips(it) */ },
        onSpeedChange = { /* viewModel.setSpeed(it) */ },
        onSaveClick = { onBackClick() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    state: SettingsState,
    onBackClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onReminderToggle: (Boolean) -> Unit,
    onTipsToggle: (Boolean) -> Unit,
    onSpeedChange: (Float) -> Unit,
    onSaveClick: () -> Unit
) {
    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("SETTINGS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DarkBackground)
            )
        },
        bottomBar = {
            // Кнопка Save внизу
            Button(
                onClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                shape = RoundedCornerShape(50) // Полностью круглая
            ) {
                Text("SAVE ALL CHANGES", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // --- Interface Section ---
            SettingsSection(title = "INTERFACE") {
                SettingsCard {
                    SettingsItem(
                        icon = Icons.Default.Language,
                        title = "App Language",
                        subtitle = "Current: ${state.currentLanguage.displayName}",
                        iconColor = NeonGreen,
                        onClick = onLanguageClick
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(state.currentLanguage.displayName.substringBefore(" "), color = NeonGreen, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary)
                        }
                    }
                }
            }

            // --- Account Section ---
            SettingsSection(title = "ACCOUNT") {
                SettingsCard {
                    SettingsItem(
                        icon = Icons.Default.Person,
                        title = "Personal Information",
                        iconColor = Color(0xFF4CAF50), // Greenish
                        onClick = {}
                    )
                    HorizontalDivider(color = DarkBackground, thickness = 1.dp)
                    SettingsItem(
                        icon = Icons.Default.Code,
                        title = "Interview Tech Stack",
                        iconColor = Color(0xFF4CAF50),
                        onClick = {}
                    )
                    HorizontalDivider(color = DarkBackground, thickness = 1.dp)
                    SettingsItem(
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        title = "Sign Out",
                        iconColor = Color(0xFFE57373), // Reddish
                        iconBgColor = Color(0xFFE57373).copy(alpha = 0.1f),
                        textColor = Color(0xFFE57373),
                        showChevron = false,
                        onClick = {}
                    )
                }
            }

            // --- Alerts Section ---
            SettingsSection(title = "ALERTS") {
                SettingsCard {
                    SettingsSwitchItem(
                        icon = Icons.Default.Notifications,
                        title = "Interview Reminders",
                        subtitle = "Daily practice nudges",
                        isChecked = state.isRemindersEnabled,
                        onCheckedChange = onReminderToggle
                    )
                    HorizontalDivider(color = DarkBackground, thickness = 1.dp)
                    SettingsSwitchItem(
                        icon = Icons.Default.Lightbulb,
                        title = "Daily Tech Tips",
                        subtitle = "Flashcards for algorithm prep",
                        isChecked = state.isTechTipsEnabled,
                        onCheckedChange = onTipsToggle
                    )
                }
            }

            // --- Voice & AI Section ---
            SettingsSection(title = "VOICE & AI") {
                SettingsCard {
                    SettingsItem(
                        icon = Icons.Default.RecordVoiceOver,
                        title = "Interviewer Voice",
                        subtitle = "\"Modern Tech Expert (Male)\"",
                        iconColor = Color(0xFF4CAF50),
                        onClick = {}
                    )
                    HorizontalDivider(color = DarkBackground, thickness = 1.dp)
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            SettingsIcon(icon = Icons.Default.Speed, color = Color(0xFF4CAF50))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Speech Speed", color = Color.White, fontSize = 16.sp)
                                Text("${state.speechSpeed}x", color = TextSecondary, fontSize = 12.sp)
                            }
                            Text(
                                if(state.speechSpeed == 1.0f) "Normal (1.0x)"
                                else String.format("%.1fx", state.speechSpeed),
                                color = NeonGreen,
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Slider(
                            value = state.speechSpeed,
                            onValueChange = onSpeedChange,
                            valueRange = 0.5f..2.0f,
                            steps = 2, // 0.5, 1.0, 1.5, 2.0
                            colors = SliderDefaults.colors(
                                thumbColor = NeonGreen,
                                activeTrackColor = NeonGreen,
                                inactiveTrackColor = DarkBackground
                            )
                        )
                    }
                }
            }

            // --- Application Section ---
            SettingsSection(title = "APPLICATION") {
                SettingsCard {
                    SettingsItem(icon = Icons.Default.Description, title = "Privacy Policy", iconColor = TextSecondary, onClick = {}) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                    }
                    HorizontalDivider(color = DarkBackground, thickness = 1.dp)
                    SettingsItem(icon = Icons.Default.Gavel, title = "Terms of Service", iconColor = TextSecondary, onClick = {}) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                    }
                    HorizontalDivider(color = DarkBackground, thickness = 1.dp)
                    SettingsItem(
                        icon = Icons.Default.Info,
                        title = "Build Version",
                        subtitle = "Production Release ${state.appVersion}\n(Stable)",
                        iconColor = TextSecondary,
                        onClick = {}
                    ) {
                        Text("v${state.appVersion}", color = TextSecondary, fontSize = 12.sp)
                    }
                }
            }

            // Footer text
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).background(NeonGreen, CircleShape))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SYSTEM READY FOR INTERVIEW", color = TextSecondary, fontSize = 10.sp, letterSpacing = 2.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("© 2026 AI TECH INTERVIEWER. ALL RIGHTS", color = TextSecondary.copy(alpha = 0.5f), fontSize = 10.sp)
                Text("RESERVED. CODED FOR PRECISION.", color = TextSecondary.copy(alpha = 0.5f), fontSize = 10.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    val mockState = SettingsState(
        currentLanguage = AppLanguage.ENGLISH,
        isRemindersEnabled = true,
        isTechTipsEnabled = false,
        speechSpeed = 1.0f
    )

    SettingsScreenContent(
        state = mockState,
        onBackClick = {},
        onLanguageClick = {},
        onReminderToggle = {},
        onTipsToggle = {},
        onSpeedChange = {},
        onSaveClick = {}
    )
}