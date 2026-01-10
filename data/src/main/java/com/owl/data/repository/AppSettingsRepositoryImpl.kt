package com.owl.data.repository

import com.owl.data.db.dao.AppSettingsDao
import com.owl.data.db.entity.AppSettingsEntity
import com.owl.domain.model.AppLanguage
import com.owl.domain.port.repository.AppSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class AppSettingsRepositoryImpl(
    private val dao: AppSettingsDao
) : AppSettingsRepository {

    override fun getLanguage(): Flow<AppLanguage> {
        return dao.getSettings().map { entity ->
            AppLanguage.fromCode(entity?.languageCode ?: AppLanguage.ENGLISH.code)
        }
    }

    override suspend fun getCurrentLanguage(): AppLanguage {
        val entity = dao.getSettings().firstOrNull()
        return AppLanguage.fromCode(entity?.languageCode ?: AppLanguage.ENGLISH.code)
    }

    override suspend fun setLanguage(language: AppLanguage) {
        dao.saveSettings(AppSettingsEntity(languageCode = language.code))
    }
}