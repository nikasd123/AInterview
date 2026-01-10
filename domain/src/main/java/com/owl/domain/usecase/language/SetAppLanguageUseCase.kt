package com.owl.domain.usecase.language

import com.owl.domain.model.AppLanguage
import com.owl.domain.port.repository.AppSettingsRepository

class SetAppLanguageUseCase(private val repository: AppSettingsRepository) {
    suspend operator fun invoke(language: AppLanguage) = repository.setLanguage(language)
}