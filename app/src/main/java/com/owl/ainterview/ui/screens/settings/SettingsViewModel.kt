package com.owl.ainterview.ui.screens.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owl.domain.model.AppLanguage
import com.owl.domain.usecase.language.GetAppLanguageUseCase
import com.owl.domain.usecase.language.SetAppLanguageUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    getAppLanguageUseCase: GetAppLanguageUseCase,
    private val setAppLanguageUseCase: SetAppLanguageUseCase
) : ViewModel() {

    val currentLanguage: StateFlow<AppLanguage> = getAppLanguageUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            AppLanguage.ENGLISH
        )

    fun onLanguageSelected(language: AppLanguage) {
        viewModelScope.launch {
            setAppLanguageUseCase(language)

            val localeList = LocaleListCompat.forLanguageTags(language.code)
            AppCompatDelegate.setApplicationLocales(localeList)
        }
    }
}