package com.owl.ainterview.ui.screens.settings

import android.os.Build
import com.data.owl.ainterview.BuildConfig
import com.owl.domain.model.AppLanguage

data class SettingsState(
    val currentLanguage: AppLanguage = AppLanguage.ENGLISH,
    val isRemindersEnabled: Boolean = true,
    val isTechTipsEnabled: Boolean = false,
    val speechSpeed: Float = 1.0f,
    val appVersion: String = BuildConfig.LIBRARY_PACKAGE_NAME //todo Version
)