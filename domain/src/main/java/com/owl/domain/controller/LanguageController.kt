package com.owl.domain.controller

import com.owl.domain.model.AppLanguage
import kotlinx.coroutines.flow.Flow

interface LanguageController {
    suspend fun setLanguage(language: AppLanguage)
    suspend fun synchronize()
    fun getCurrentLanguage(): Flow<AppLanguage>
}