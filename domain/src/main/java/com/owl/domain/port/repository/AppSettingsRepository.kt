package com.owl.domain.port.repository

import com.owl.domain.model.AppLanguage
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {
    fun getLanguage(): Flow<AppLanguage>
    suspend fun setLanguage(language: AppLanguage)
    suspend fun getCurrentLanguage(): AppLanguage
}