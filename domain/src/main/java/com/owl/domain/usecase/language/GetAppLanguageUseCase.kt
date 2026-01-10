package com.owl.domain.usecase.language

import com.owl.domain.model.AppLanguage
import com.owl.domain.port.repository.AppSettingsRepository
import kotlinx.coroutines.flow.Flow

class GetAppLanguageUseCase(private val repository: AppSettingsRepository) {
    operator fun invoke(): Flow<AppLanguage> = repository.getLanguage()
}