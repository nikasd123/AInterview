package com.owl.data.controller

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.owl.domain.controller.LanguageController
import com.owl.domain.model.AppLanguage
import com.owl.domain.port.repository.AppSettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LanguageControllerImpl(
    private val repository: AppSettingsRepository
) : LanguageController {

    override fun getCurrentLanguage(): Flow<AppLanguage> = repository.getLanguage()

    override suspend fun setLanguage(language: AppLanguage) {
        repository.setLanguage(language)
        applyToSystem(language)
    }

    override suspend fun synchronize() {
        val dbLang = repository.getCurrentLanguage()
        val systemLangCode = getSystemLocaleTag()

        if (systemLangCode != dbLang.code) {
            applyToSystem(dbLang)
        }
    }

    private suspend fun applyToSystem(language: AppLanguage) = withContext(Dispatchers.Main) {
        val localeList = LocaleListCompat.forLanguageTags(language.code)
        val currentLocales = AppCompatDelegate.getApplicationLocales()
        val currentTag = if (!currentLocales.isEmpty) currentLocales[0]?.toLanguageTag() else ""

        currentTag?.startsWith(language.code)?.let {
            if (!it) {
                AppCompatDelegate.setApplicationLocales(localeList)
            }
        }
    }

    private fun getSystemLocaleTag(): String {
        val loc = AppCompatDelegate.getApplicationLocales()
        return if (!loc.isEmpty) loc[0]?.language ?: "en" else "en"
    }
}