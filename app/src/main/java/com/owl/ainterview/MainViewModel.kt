package com.owl.ainterview

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.owl.domain.model.AppLanguage
import com.owl.domain.usecase.language.GetAppLanguageUseCase
import com.owl.domain.usecase.language.SetAppLanguageUseCase
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class MainViewModel(
    private val getAppLanguageUseCase: GetAppLanguageUseCase,
    private val setAppLanguageUseCase: SetAppLanguageUseCase
) : ViewModel() {

    init {
        syncInitialState()
        observeLanguageChanges()
    }

    private fun syncInitialState() {
        val currentAppLocales = AppCompatDelegate.getApplicationLocales()
        if (!currentAppLocales.isEmpty) {
            val systemTag = currentAppLocales[0]?.language
            val savedLanguage = AppLanguage.fromCode(systemTag)

            viewModelScope.launch {
                setAppLanguageUseCase(savedLanguage)
            }
        }
    }

    private fun observeLanguageChanges() {
        viewModelScope.launch {
            getAppLanguageUseCase()
                .distinctUntilChanged()
                .collect { appLanguage ->
                    setSystemLocale(appLanguage)
                }
        }
    }

    private fun setSystemLocale(appLanguage: AppLanguage) {
        val currentLocales = AppCompatDelegate.getApplicationLocales()
        val currentTag = if (!currentLocales.isEmpty) currentLocales[0]?.language else ""

        if (currentTag != appLanguage.code) {
            val localeList = LocaleListCompat.forLanguageTags(appLanguage.code)
            AppCompatDelegate.setApplicationLocales(localeList)
        }
    }
}