package com.owl.data.di

import androidx.room.Room
import com.owl.ainterview.BuildConfig
import com.owl.data.db.AppDatabase
import com.owl.data.repository.SessionRepositoryImpl
import com.owl.data.service.AiInterviewerServiceImpl
import com.owl.data.service.SpeechServiceImpl
import com.owl.data.service.TtsServiceImpl
import com.owl.domain.port.repository.SessionRepository
import com.owl.domain.port.service.AiInterviewerService
import com.owl.domain.port.service.SpeechService
import com.owl.domain.port.service.TtsService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<AiInterviewerService> {
        AiInterviewerServiceImpl(apiKey = BuildConfig.GEMINI_API_KEY)
    }

    factory<SpeechService> {
        SpeechServiceImpl(context = androidContext())
    }

    single<TtsService> {
        TtsServiceImpl(context = androidContext())
    }

    //--- DB ---
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "ai_interviewer.db"
        ).build()
    }

    single { get<AppDatabase>().sessionDao() }
    single<SessionRepository> { SessionRepositoryImpl(dao = get()) }
}