package com.owl.ainterview.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owl.domain.controller.LanguageController
import com.owl.domain.model.AppLanguage
import com.owl.domain.usecase.language.GetAppLanguageUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    getAppLanguageUseCase: GetAppLanguageUseCase,
    private val languageController: LanguageController
) : ViewModel() {

    val currentLanguage: StateFlow<AppLanguage> = getAppLanguageUseCase()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            AppLanguage.ENGLISH
        )

    fun onLanguageSelected(language: AppLanguage) {
        viewModelScope.launch {
            languageController.setLanguage(language)
        }
    }
}