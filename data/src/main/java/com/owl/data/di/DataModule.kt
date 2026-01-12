package com.owl.data.di

import androidx.room.Room
import com.data.owl.ainterview.BuildConfig
import com.owl.data.db.AppDatabase
import com.owl.data.repository.AppSettingsRepositoryImpl
import com.owl.data.repository.SessionRepositoryImpl
import com.owl.data.service.AiInterviewerServiceImpl
import com.owl.data.service.SpeechServiceImpl
import com.owl.data.service.TtsServiceImpl
import com.owl.domain.port.repository.AppSettingsRepository
import com.owl.domain.port.repository.SessionRepository
import com.owl.domain.port.service.AiInterviewerService
import com.owl.domain.port.service.SpeechService
import com.owl.domain.port.service.TtsService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    // --- API Services ---
    single<AiInterviewerService> {
        AiInterviewerServiceImpl(apiKey = BuildConfig.GEMINI_API_KEY)
    }

    // --- Hardware Services ---
    factory<SpeechService> {
        SpeechServiceImpl(context = androidContext())
    }

    single<TtsService> {
        TtsServiceImpl(context = androidContext())
    }

    // --- Database ---
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "ai_interviewer.db"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    // DAOs
    single { get<AppDatabase>().sessionDao() }
    single { get<AppDatabase>().appSettingsDao() }

    // Repositories
    single<SessionRepository> { SessionRepositoryImpl(dao = get()) }
    single<AppSettingsRepository> { AppSettingsRepositoryImpl(dao = get()) }
}