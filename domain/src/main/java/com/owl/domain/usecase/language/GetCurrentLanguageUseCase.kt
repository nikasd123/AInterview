package com.owl.domain.usecase.language

import com.owl.domain.model.AppLanguage
import com.owl.domain.port.repository.AppSettingsRepository

class GetCurrentLanguageUseCase(private val repository: AppSettingsRepository) {
    suspend operator fun invoke(): AppLanguage = repository.getCurrentLanguage()
}