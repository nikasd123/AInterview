package com.owl.domain.di

import com.owl.domain.usecase.GenerateQuestionsUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GenerateQuestionsUseCase(aiService = get()) }
}