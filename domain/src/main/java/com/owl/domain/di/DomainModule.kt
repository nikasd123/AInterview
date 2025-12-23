package com.owl.domain.di

import com.owl.domain.usecase.GenerateQuestionsUseCase
import com.owl.domain.usecase.GetSessionUseCase
import com.owl.domain.usecase.ProcessAnswerUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GenerateQuestionsUseCase(aiService = get(), sessionRepository = get()) }
    factory { GetSessionUseCase(repository = get()) }
    factory { ProcessAnswerUseCase(aiService = get(), sessionRepository = get()) }
}