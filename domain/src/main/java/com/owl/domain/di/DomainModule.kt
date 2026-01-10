package com.owl.domain.di

import com.owl.domain.usecase.GenerateQuestionsUseCase
import com.owl.domain.usecase.GetHomeDataUseCase
import com.owl.domain.usecase.GetSessionUseCase
import com.owl.domain.usecase.ProcessAnswerUseCase
import com.owl.domain.usecase.language.GetAppLanguageUseCase
import com.owl.domain.usecase.language.GetCurrentLanguageUseCase
import com.owl.domain.usecase.language.SetAppLanguageUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GenerateQuestionsUseCase(
        aiService = get(), sessionRepository = get(),
        getCurrentLanguageUseCase = get(),
    ) }
    factory { GetSessionUseCase(repository = get()) }
    factory { ProcessAnswerUseCase(
        aiService = get(), sessionRepository = get(),
        getCurrentLanguageUseCase = get()
    ) }
    factory { GetHomeDataUseCase(repository = get()) }
    factory { GetAppLanguageUseCase(repository = get()) }
    factory { GetCurrentLanguageUseCase(repository = get()) }
    factory { SetAppLanguageUseCase(repository = get()) }
}