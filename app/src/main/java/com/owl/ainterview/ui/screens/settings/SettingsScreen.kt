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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.owl.ainterview.R
import com.owl.ainterview.ui.screens.settings.components.SettingsCard
import com.owl.ainterview.ui.screens.settings.components.SettingsIcon
import com.owl.ainterview.ui.screens.settings.components.SettingsItem
import com.owl.ainterview.ui.screens.settings.components.SettingsSection
import com.owl.ainterview.ui.screens.settings.components.SettingsSwitchItem
import com.owl.ainterview.ui.theme.Dimens
import com.owl.ainterview.ui.theme.NeonGreen
import com.owl.ainterview.ui.theme.SettingsDivider
import com.owl.ainterview.ui.theme.SettingsIconGreen
import com.owl.ainterview.ui.theme.SettingsIconRed
import com.owl.ainterview.ui.theme.SettingsIconRedBg
import com.owl.ainterview.ui.theme.TextPrimary
import com.owl.ainterview.ui.theme.TextSecondary
import com.owl.domain.model.AppLanguage
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onNavigateToLanguageSelection: () -> Unit,
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
    onSaveClick: () -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.settings_title),
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = Dimens.TextSizeLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            // Кнопка Save внизу
            Button(
                onClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens.PaddingStandard)
                    .height(Dimens.ButtonHeightStandard),
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                shape = RoundedCornerShape(Dimens.CornerRound)
            ) {
                Text(
                    stringResource(R.string.settings_save_changes),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Dimens.PaddingStandard),
            verticalArrangement = Arrangement.spacedBy(Dimens.PaddingLarge)
        ) {
            // --- Interface Section ---
            SettingsSection(title = stringResource(R.string.section_interface)) {
                SettingsCard {
                    SettingsItem(
                        icon = Icons.Default.Language,
                        title = stringResource(R.string.pref_app_language),
                        subtitle = stringResource(
                            R.string.pref_current_lang_format,
                            state.currentLanguage.displayName
                        ),
                        iconColor = NeonGreen,
                        onClick = onLanguageClick
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                state.currentLanguage.displayName.substringBefore(" "),
                                color = NeonGreen,
                                fontSize = Dimens.TextSizeStandard
                            )
                            Spacer(modifier = Modifier.width(Dimens.PaddingMedium))
                            Icon(
                                Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = TextSecondary
                            )
                        }
                    }
                }
            }

            // --- Account Section ---
            SettingsSection(title = stringResource(R.string.section_account)) {
                SettingsCard {
                    SettingsItem(
                        icon = Icons.Default.Person,
                        title = stringResource(R.string.pref_personal_info),
                        iconColor = SettingsIconGreen,
                        onClick = {}
                    )
                    HorizontalDivider(color = SettingsDivider, thickness = Dimens.BorderThin)
                    SettingsItem(
                        icon = Icons.Default.Code,
                        title = stringResource(R.string.pref_tech_stack),
                        iconColor = SettingsIconGreen,
                        onClick = {}
                    )
                    HorizontalDivider(color = SettingsDivider, thickness = Dimens.BorderThin)
                    SettingsItem(
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        title = stringResource(R.string.pref_sign_out),
                        iconColor = SettingsIconRed,
                        iconBgColor = SettingsIconRedBg,
                        textColor = SettingsIconRed,
                        showChevron = false,
                        onClick = {}
                    )
                }
            }

            // --- Alerts Section ---
            SettingsSection(title = stringResource(R.string.section_alerts)) {
                SettingsCard {
                    SettingsSwitchItem(
                        icon = Icons.Default.Notifications,
                        title = stringResource(R.string.pref_reminders),
                        subtitle = stringResource(R.string.pref_reminders_sub),
                        isChecked = state.isRemindersEnabled,
                        onCheckedChange = onReminderToggle
                    )
                    HorizontalDivider(color = SettingsDivider, thickness = Dimens.BorderThin)
                    SettingsSwitchItem(
                        icon = Icons.Default.Lightbulb,
                        title = stringResource(R.string.pref_tech_tips),
                        subtitle = stringResource(R.string.pref_tech_tips_sub),
                        isChecked = state.isTechTipsEnabled,
                        onCheckedChange = onTipsToggle
                    )
                }
            }

            // --- Voice & AI Section ---
            SettingsSection(title = stringResource(R.string.section_voice_ai)) {
                SettingsCard {
                    SettingsItem(
                        icon = Icons.Default.RecordVoiceOver,
                        title = stringResource(R.string.pref_interviewer_voice),
                        subtitle = stringResource(R.string.pref_voice_sub),
                        iconColor = SettingsIconGreen,
                        onClick = {}
                    )
                    HorizontalDivider(color = SettingsDivider, thickness = Dimens.BorderThin)
                    Column(modifier = Modifier.padding(Dimens.PaddingStandard)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            SettingsIcon(icon = Icons.Default.Speed, color = SettingsIconGreen)
                            Spacer(modifier = Modifier.width(Dimens.PaddingStandard))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    stringResource(R.string.pref_speech_speed),
                                    color = TextPrimary,
                                    fontSize = Dimens.TextSizeLarge
                                )
                                Text(
                                    "${state.speechSpeed}x",
                                    color = TextSecondary,
                                    fontSize = Dimens.TextSizeSmall
                                )
                            }
                            Text(
                                if (state.speechSpeed == 1.0f) stringResource(R.string.pref_speed_normal)
                                else stringResource(
                                    R.string.pref_speed_value_format,
                                    state.speechSpeed
                                ),
                                color = NeonGreen,
                                fontSize = Dimens.TextSizeSmall
                            )
                        }
                        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
                        Slider(
                            value = state.speechSpeed,
                            onValueChange = onSpeedChange,
                            valueRange = 0.5f..2.0f,
                            steps = Dimens.SliderSteps,
                            colors = SliderDefaults.colors(
                                thumbColor = NeonGreen,
                                activeTrackColor = NeonGreen,
                                inactiveTrackColor = MaterialTheme.colorScheme.background
                            )
                        )
                    }
                }
            }

            // --- Application Section ---
            SettingsSection(title = stringResource(R.string.section_application)) {
                SettingsCard {
                    SettingsItem(
                        icon = Icons.Default.Description,
                        title = stringResource(R.string.pref_privacy_policy),
                        iconColor = TextSecondary,
                        onClick = {}) {
                        Icon(
                            Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(Dimens.SettingsEndIconSize)
                        )
                    }
                    HorizontalDivider(color = SettingsDivider, thickness = Dimens.BorderThin)
                    SettingsItem(
                        icon = Icons.Default.Gavel,
                        title = stringResource(R.string.pref_terms_service),
                        iconColor = TextSecondary,
                        onClick = {}) {
                        Icon(
                            Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(Dimens.SettingsEndIconSize)
                        )
                    }
                    HorizontalDivider(color = SettingsDivider, thickness = Dimens.BorderThin)
                    SettingsItem(
                        icon = Icons.Default.Info,
                        title = stringResource(R.string.pref_build_version),
                        subtitle = stringResource(R.string.pref_build_sub_format, state.appVersion),
                        iconColor = TextSecondary,
                        onClick = {}
                    ) {
                        Text(
                            stringResource(R.string.pref_version_format, state.appVersion),
                            color = TextSecondary,
                            fontSize = Dimens.TextSizeSmall
                        )
                    }
                }
            }

            // Footer text
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(Dimens.FooterDotSize)
                            .background(NeonGreen, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(Dimens.PaddingMedium))
                    Text(
                        stringResource(R.string.footer_system_ready),
                        color = TextSecondary,
                        fontSize = Dimens.TextSizeMicro,
                        letterSpacing = 2.sp
                    )
                }
                Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
                Text(
                    stringResource(R.string.footer_copyright_1),
                    color = TextSecondary.copy(alpha = 0.5f),
                    fontSize = Dimens.TextSizeMicro
                )
                Text(
                    stringResource(R.string.footer_copyright_2),
                    color = TextSecondary.copy(alpha = 0.5f),
                    fontSize = Dimens.TextSizeMicro
                )
            }

            Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
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